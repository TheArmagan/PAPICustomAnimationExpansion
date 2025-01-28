package rest.armagan.papiexpansions

import com.google.common.cache.CacheBuilder
import me.clip.placeholderapi.PlaceholderAPI
import me.clip.placeholderapi.PlaceholderAPIPlugin
import me.clip.placeholderapi.expansion.Cacheable
import me.clip.placeholderapi.expansion.PlaceholderExpansion
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.entity.Player
import java.io.File
import java.util.concurrent.TimeUnit

class PAPICustomAnimationExpansion : PlaceholderExpansion(), Cacheable {
    private var folder: File = File("${PlaceholderAPIPlugin.getInstance().dataFolder}/animations/")
    private var cache = CacheBuilder.newBuilder()
        .expireAfterWrite(1, TimeUnit.MINUTES)
        .build<String, TextAnimation>();

    init {
        if (!folder.exists()) {
            folder.mkdirs();
            val exampleFile = File(folder, "example.yml");
            exampleFile.writeText(
                """
                # example.yml
                # File name is animation name
                # Example Usage: %animation_example:ExampleInputVar%
                # Example template: %animation_<fileNameWithoutExt>[:var1[:var2[...]]]%
                # Supports PlaceholderAPI placeholders
                # Supports nested animation placeholders
                default_duration: 500
                frames:
                  - text: "Frame 1 (500ms) {0} %player_name%"
                    duration: 500
                  - text: "Frame 2 (1000ms)"
                    duration: 1000
                  - text: "Frame 3 (1500ms)"
                    duration: 1500
                  - text: "Frame 4 (500ms (default))"
                  - text: "Frame 5 (500ms (default))"
                  # - text: "<rainbow:%animation_incfloat%>||||||||||||||||||||||||</rainbow>"
                  #   duration: 3000
                  # - text: "<gradient:green:blue:%animation_incint%>||||||||||||||||||||||||</gradient>"
                  #   duration: 3000
                """.trimIndent()
            );
        }
    }

    override fun getIdentifier(): String {
        return "animation"
    }

    override fun getAuthor(): String {
        return "TheArmagan"
    }

    override fun getVersion(): String {
        return "0.0.1"
    }

    override fun onPlaceholderRequest(player: Player?, identifier: String): String {
        val replaced = PlaceholderAPI.setBracketPlaceholders(player, identifier)
        val splited = replaced.split(":")

        if (splited.isEmpty()) return "";

        val animationName = splited[0];

        val animation = cache.get(animationName) {
            val file = File(folder, "$animationName.yml")
            if (!file.exists()) return@get TextAnimation(1000, listOf(TextAnimationFrame("Unable to load animation.", null)));
            return@get YamlConfiguration.loadConfiguration(file).run {
                TextAnimation(
                    defaultDuration =  getInt("default_duration"),
                    frames = getMapList("frames").map {
                        TextAnimationFrame(
                            it["text"] as String,
                            it["duration"] as Int?
                        )
                    }
                )
            }
        };

        var frame = animation.getFrame().text;

        splited.forEachIndexed { index, value ->
            frame = frame.replace("{$index}", value)
        }

        frame = PlaceholderAPI.setPlaceholders(player, frame);

        return frame;
    }

    override fun clear() {
        cache.invalidateAll()
    }
}