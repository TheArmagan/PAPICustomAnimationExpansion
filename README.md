# PAPICustomAnimationExpansion
 This expansion allows you to do nested animations and keyframed text animations.


```yml
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
```