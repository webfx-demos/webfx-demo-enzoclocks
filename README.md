# Live demo

The demo is published on [this page][demo-live-link].

It displays different clocks in different time zones.
You can add as many clocks as you want using the plus button.
They are arranged on the screen using a circles packer algorithm. 
You can click on the clocks to change their settings (time zone, caption & design).
You can clock on the gear icon to change the global settings (ex: background).

# Highlighted features

## Reusability

This demo demonstrates how you can reuse existing libraries written in JavaFX in a WebFX application.
Here, it is using [Enzo][hansolo-enzo-link], a JavaFX library which implements many controls, and in particular this clock control with 4 different designs.
The flip effect when clicking on a clock to display the settings on its back is also taken from this library.

## Responsive design

JavaFX has a great feature to implement responsive design in your application (as already described in this [previous demo][demo-responsive-link]).

For example, this demo layouts the clocks according to the window width & height using a circles packer algorithm,
a performance that is not achievable with a standard responsive design approach based on CSS rules. 

[demo-live-link]: https://enzoclocks.webfx.dev
[demo-source-link]: https://github.com/webfx-project/webfx/blob/master/webfx-demos/webfx-demo-enzoclocks/webfx-demo-enzoclocks-application/src/main/java/dev/webfx/demo/enzoclocks/EnzoClocksApplication.java
[hansolo-enzo-link]: https://bitbucket.org/hansolo/enzo/src
[demo-responsive-link]: https://github.com/webfx-project/webfx-demo-tallycounter#responsive-design