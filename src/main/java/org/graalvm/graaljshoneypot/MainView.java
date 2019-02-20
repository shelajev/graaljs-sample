package org.graalvm.graaljshoneypot;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.router.Route;
import org.graalvm.graaljshoneypot.graaljs.ExecutionResult;
import org.graalvm.graaljshoneypot.graaljs.Graaljs;

@Route
public class MainView extends VerticalLayout {

    private static final String defaultScript = "var a = 1 + 2; \n" +
            "console.log(\"a = \" + a);\n" +
            "a + 39;";


    public MainView() {
        setSizeFull();
        setDefaultHorizontalComponentAlignment(Alignment.CENTER);
        addClassName("main-view");

        TextArea code = new TextArea("code");
        code.setValue(defaultScript);

        code.setWidth("100%");
        code.setAutofocus(true);
        code.setHeight("400px");


        TextArea log = new TextArea("log");
        log.setWidth("100%");
        log.setHeight("400px");

        HorizontalLayout buttons = new HorizontalLayout();
        buttons.add(new Button("Run", event -> {
            String script = code.getValue();

            ExecutionResult eval = Graaljs.eval(script);
            log.setValue(eval.output + "\n\n > " + eval.returnValue + "\n\n " + eval.time);
        }));
        buttons.add(new Button("Clear", e -> {
            Notification.show("Boom, a clean slate!");
            code.setValue("");
            log.setValue("");
        }));
        add(buttons);


        HorizontalLayout main = new HorizontalLayout();
        main.setSizeFull();
        main.add(code);

        log.setEnabled(false);
        main.add(log);

        add(main);
    }

}
