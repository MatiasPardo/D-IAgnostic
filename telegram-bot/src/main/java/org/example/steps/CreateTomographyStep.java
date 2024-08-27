package org.example.steps;

import org.example.bot.Bot;
import org.example.bot.SingletonBot;
import org.example.client.HttpClient;
import org.example.entities.Tomography;
import org.example.entities.User;
import org.example.exceptions.InvalidOptionException;
import org.example.messages.MessageBuilder;
import org.example.repositories.UserRepository;
import org.telegram.telegrambots.meta.api.methods.GetFile;
import org.telegram.telegrambots.meta.api.objects.File;
import org.telegram.telegrambots.meta.api.objects.PhotoSize;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.io.InputStream;
import java.net.URL;
import java.util.List;

public class CreateTomographyStep extends Step {
    private int step = 1;
    private String titleOrId;

    public CreateTomographyStep(String titleOrId) {
        this.titleOrId = titleOrId;
    }

    public String executeStep(Update update) {
        var userId = update.getMessage().getFrom().getId();
        if(update.getMessage().hasPhoto()){
            step = 4;
        }
        switch (step) {
            case 1 -> {
                var message = getMessage();
                step++;
                return message;
            }
            case 2 -> {
                var value = validateAndGetOption(update);
                if (value.equals("0")) {
                    return resetStep(userId).getMessage();
                }
                var message = getMessage();
                step++;
                return message;
            }
            case 3 -> {
                titleOrId = update.getMessage().getText();
                this.addStepPhoto(userId, titleOrId);
                var message = getMessage();
                step++;
                return message;
            }
            case 4 -> {
                this.saveTomography(update, userId);
                var message = getMessage();
                this.resetStep(userId);
                return message;
            }
        }
        if (step > 3) {
            this.resetStep(userId);
            throw new InvalidOptionException("Opción no válida");
        }
        return null;
    }

    @Override
    public String getMessage() {
        switch (step) {
            case 1 -> {
                var message = MessageBuilder.builder();
                return message
                        .withLine("0. Volver al menú principal")
                        .withLine("1. Enviar imagenes para analizar")
                        .build();
            }
            case 2 -> {
                return  "Ingresá el titulo para el diagnostico";
            }
            case 3 -> {
                return "Ingrese una imagen para analizar";
            }
            case 4 ->{
                var message = MessageBuilder.builder();
                return message
                        .withLine("Cuando se finalice en analisis se enviara el estudio, si pierde la sesion puede recuperar el informe desde la opcion de ver todos mis informes")
                        .withLine("Seleccioná una opción")
                        .withLine("1. Ver todos mis informes")
                        .withLine("2. Solicitar un nuevo informe")
                        .withLine("3. Cerrar sesión")
                        .build();
            }
        }
        throw new RuntimeException("Opción no válida");
    }

    public String validateAndGetOption(Update update) {
        var option = update.getMessage().getText().trim();
        if (!option.equals("0") && !option.equals("1")) {
            throw new InvalidOptionException("Opción no válida");
        }
        return option;
    }

    public String postSaveTomography(String tittle, byte[] tac, long userId){
        var client = new HttpClient();
        var user = UserRepository.get(userId);
        return client.saveTomography(user.getJwt(), tittle, tac);
    }

    private String saveTomography(Update update, Long userId) {
        List<PhotoSize> photos = update.getMessage().getPhoto();
        PhotoSize photo = photos.get(photos.size() - 1);
        String fileId = photo.getFileId();
        try{
            Bot bot = SingletonBot.singletonBot.get("bot");
            File file = bot.execute(new GetFile(fileId));
            String filePath = file.getFilePath();
            String fileUrl = "https://api.telegram.org/file/bot" + bot.getBotToken() + "/" + filePath;
            InputStream in = new URL(fileUrl).openStream();
            var client = new HttpClient();
            var user = UserRepository.get(userId);
            String codeReport = client.saveTomography(user.getJwt(), titleOrId, in.readAllBytes());
            this.buscarResultado(user, codeReport, client);
            return codeReport;
        }catch(Exception e){
            e.getStackTrace();
        }
        return null;
    }

    private void buscarResultado(User user, String codeReport, HttpClient client) throws InterruptedException {
        Tomography tomography = client.getTomographyReport(user.getJwt(), codeReport);
        if(!tomography.getStatusReport().equals(Tomography.StatusReport.INFORME_GENERADO)){
            Thread.sleep(10000);
            System.out.println("Se busca el estado");
            buscarResultado(user, codeReport, client);
        }
    }
}
