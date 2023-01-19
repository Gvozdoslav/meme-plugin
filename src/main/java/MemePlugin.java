import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

public class MemePlugin extends AnAction {

    private String filePath;

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {

        MemeProcessor memeProcessor = new MemeProcessor();

        SimpleGuiForm simpleGuiForm = new SimpleGuiForm(memeProcessor);
        ImageFormProcessor imageFormProcessor = new ImageFormProcessor();

        CompletableFuture
                .supplyAsync(simpleGuiForm::getMemePath)
                .orTimeout(10000, TimeUnit.MILLISECONDS)
                .whenComplete((res, ex) -> {
                    if (ex != null)
                        ex.printStackTrace();
                    if (res != null)
                        imageFormProcessor.load(res);
                });

    }
}
