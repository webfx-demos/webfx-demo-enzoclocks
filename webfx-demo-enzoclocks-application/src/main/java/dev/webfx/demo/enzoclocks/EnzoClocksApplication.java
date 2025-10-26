package dev.webfx.demo.enzoclocks;

import dev.webfx.demo.enzoclocks.settings.BackgroundMenuPane;
import dev.webfx.demo.enzoclocks.settings.ClockSetting;
import dev.webfx.demo.enzoclocks.settings.SvgButtonPaths;
import dev.webfx.extras.led.PlusLed;
import dev.webfx.extras.panes.ScalePane;
import dev.webfx.kit.util.properties.FXProperties;
import dev.webfx.kit.util.scene.DeviceSceneUtil;
import dev.webfx.lib.circlepacking.CirclePackingPane;
import dev.webfx.platform.storage.LocalStorage;
import dev.webfx.platform.uischeduler.UiScheduler;
import eu.hansolo.enzo.clock.Clock;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.SVGPath;
import javafx.stage.Stage;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Bruno Salmon
 */
public final class EnzoClocksApplication extends Application {

    private final List<ClockSetting> clockSettings = new ArrayList<>();
    private final CirclePackingPane circlePackingPane = new CirclePackingPane(true);

    private BackgroundMenuPane backgroundMenuPane;

    @Override
    public void start(Stage stage) {
        // Creating the plus button
        PlusLed plusLed = new PlusLed(Color.GREEN.brighter());
        StackPane.setAlignment(plusLed, Pos.BOTTOM_RIGHT);

        // Creating the gear button
        Pane gearPane = createSVGButton(SvgButtonPaths.getGearPath(), Color.GRAY);
        StackPane.setAlignment(gearPane, Pos.BOTTOM_LEFT);

        // Creating the root node
        Pane root = new StackPane(circlePackingPane, plusLed, gearPane);
        backgroundMenuPane = new BackgroundMenuPane(root);

        // Creating the scene
        Scene scene = DeviceSceneUtil.newScene(root,800, 600);
        stage.setTitle("Enzo Clocks");
        stage.setScene(scene);
        stage.show();

        // Loading state
        loadState();
        // Saving background when changed
        backgroundMenuPane.rootBackgroundGradientProperty().addListener((observable, oldValue, newValue) -> saveState());

        // Setting action handlers for the buttons
        plusLed.setOnAction(e -> addClock(ClockSetting.createRandom(clockSettings), true));
        gearPane.setOnMouseClicked(e -> root.getChildren().add(backgroundMenuPane));
        gearPane.setCursor(Cursor.HAND);

        // Resizing the buttons (percentage of scene dimensions)
        FXProperties.runNowAndOnPropertiesChange(() -> {
            double buttonSize = 0.08 * Math.min(stage.getWidth(), stage.getHeight());
            plusLed.setMaxSize(buttonSize, buttonSize);
            gearPane.setMaxSize(buttonSize, buttonSize);
            // Also their margin
            Insets buttonsMargin = new Insets(buttonSize / 10);
            StackPane.setMargin(plusLed, buttonsMargin);
            StackPane.setMargin(gearPane, buttonsMargin);
        }, scene.widthProperty(), scene.heightProperty());

        // Setting up the periodic clock timer
        updateClockTimes();
        UiScheduler.schedulePeriodic(20, this::updateClockTimes);
    }

    private Pane createSVGButton(String svgPath, Paint fill) {
        SVGPath path = new SVGPath();
        path.setContent(svgPath);
        path.setFill(fill);
        // We now embed the svg path in a pane. The reason is for a better click experience. Because in JavaFX (not in
        // the browser), the clicking area is only the filled shape, not the empty space in that shape. So when clicking
        // on a gear icon on a mobile, for example, even if globally our finger covers the icon, the final click point
        // may be in this empty space, making the button not reacting (frustrating experience).
        Pane pane = new ScalePane(path); // Will act as the mouse click area covering the entire surface
        pane.setCursor(Cursor.HAND);
        return pane;
    }

    private int discreteSecond;

    private void updateClockTimes() {
        if (!clockSettings.isEmpty()) {
            LocalTime[] clockLocalTimes = clockSettings.stream().map(s -> LocalTime.now(s.getZoneId())).toArray(LocalTime[]::new);
            boolean secondElapsed = clockLocalTimes[0].getSecond() != discreteSecond;
            for (int i = 0; i < clockSettings.size(); i++) {
                Clock clock = clockSettings.get(i).getClock();
                if (!clock.isDiscreteSecond() || secondElapsed)
                    clock.setTime(clockLocalTimes[i]);
            }
            if (secondElapsed)
                discreteSecond = clockLocalTimes[0].getSecond();
        }
    }

    private void addClocks(ClockSetting... clockSettings) {
        Arrays.stream(clockSettings).forEach(cs -> addClock(cs, false));
    }

    private void addClock(ClockSetting clockSetting, boolean save) {
        clockSettings.add(clockSetting);
        circlePackingPane.getChildren().add(clockSetting.embedClock());
        clockSetting.setOnRemoveRequested(() -> {
            clockSettings.remove(clockSetting);
            circlePackingPane.getChildren().remove(clockSetting.getContainer());
            saveState();
        });
        clockSetting.setOnStateChanged(this::saveState);
        if (save)
            saveState();
    }

    private void loadState() {
        String clocks = LocalStorage.getItem("clocks");
        if (clocks == null)
            addClocks(
                    new ClockSetting("America/Los_Angeles", "San Francisco", Clock.Design.BOSCH),
                    new ClockSetting("America/New_York", null, Clock.Design.IOS6),
                    new ClockSetting("Europe/Berlin", null, Clock.Design.DB),
                    new ClockSetting("Australia/Sydney", null, Clock.Design.BRAUN)
            );
        else {
            for (String clock : clocks.split(",")) {
                String[] token = clock.substring(1, clock.length() - 1).split(":");
                addClock(new ClockSetting(token[0], token[2], Clock.Design.valueOf(token[1])), false);
            }
        }
        String background = LocalStorage.getItem("background");
        if (background != null)
            backgroundMenuPane.setRootBackgroundGradient(background);
    }

    private void saveState() {
        String clocks = clockSettings.stream()
            .map(cs -> "{" + cs.getZoneId() + ":" + cs.getClock().getDesign() + ":" + cs.getClock().getText() + "}")
                .collect(Collectors.joining(","));
        LocalStorage.setItem("clocks", clocks);
        LocalStorage.setItem("background", backgroundMenuPane.getRootBackgroundGradient());
    }
}
