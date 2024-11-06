package prisoners;

import java.util.Map;

import org.java_websocket.WebSocket;

import com.google.gson.Gson;

public class ExperimentSession {

    private WebSocket connection;
    private Gson gson = new Gson();

    public ExperimentSession(WebSocket connection) {
        this.connection = connection;
    }

    public void startExperiment() {
        FreedomExperiment experiment = new FreedomExperiment(100);
        boolean result = experiment.run(new StepListener() {

            @Override
            public void onStep(Prisoner prisoner, Box box) {
                var response = Map.of(
                        "prisonerNumber", prisoner.number(),
                        "boxNumber", box.label(),
                        "hiddenNumber", box.hiddenNumber());

                var json = gson.toJson(response);
                connection.send(json);
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                }
            }

        });
        connection.send("Experiment result: " + result);
    }

    public void stopExperiment() {
        connection.close();
    }

}
