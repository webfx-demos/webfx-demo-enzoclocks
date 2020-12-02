# Live demo

This demo is published on [this website][demo-live-link].

It displays different clocks in different time zones.
You can add as many clocks as you want using the plus button.
They are arranged on the screen using a circles packer algorithm. 
You can click on the clocks to change their settings (time zone, caption & design).
You can clock on the gear icon to change the global settings (ex: background).

# Highlighted features

## Reusability

This demo demonstrates how you can reuse existing libraries written in JavaFx in a WebFx application.
Here, it is using [Enzo][hansolo-enzo-link], a JavaFx library which offers many controls, and in particular this clock control with 4 different designs.
The flip effect when clicking on a clock to display the settings on its back is also taken from this library.

## Responsive design

It's not well-known but JavaFx has a great feature that makes responsive design easy and powerful:
it can callback your code to let you decide how to layout your components and this at any level of the graph (which is not the case in standard HTML/JS/CSS).

For example, this demo layouts the clocks according to the window width & height using a circles packer algorithm,
a performance that is not achievable with a standard responsive design approach based on CSS rules. 

## Cross-platform

WebFx is focusing on the web port of JavaFx, but combined with other tools, you can easily produce executables (including native images) for other major platforms from your single WebFx source code.

For example, this repository has a Github workflow that automatically generates executables of this demo for Windows, MacOS, Linux, Android & iOS on each push made on the main branch.
You can see them in the current SNAPSHOT release assets.
Also the live demo is automatically updated during that workflow run.

[demo-live-link]: https://webfx-enzoclocks-demo.netlify.app
[demo-source-link]: https://github.com/webfx-project/webfx/blob/master/webfx-demos/webfx-demo-enzoclocks/webfx-demo-enzoclocks-application/src/main/java/webfx/demo/enzoclocks/EnzoClocksApplication.java
[hansolo-enzo-link]: https://bitbucket.org/hansolo/enzo/src
