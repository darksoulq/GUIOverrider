GUIOverrider is a simple plugin which allows you to change the Title of any hardcoded GUI using a simple YML configuration. It is useful for using FontImages for GUI Textures.

example:

```yml
titles:
  "Pollution Map": "My Pollution Map"
```

The plugin checks whether opened gui's title "contains" given title (meaning you dont have to specify color codes).

Depedencies:
[Protocol Lib](https://www.spigotmc.org/resources/protocollib.1997/)
