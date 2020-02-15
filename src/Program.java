import interfaces.DocumentService;
import interfaces.NotificationService;
import interfaces.TransformationService;
import models.document.DocumentDto;
import models.DtoHelper;
import services.CommandTransformationService;
import services.MemoryDocumentService;
import interfaces.CommandTransformation;
import services.InclusionTransformation;
import services.ServerSentNotificationService;

public class Program {
    public static void main(String[] args) throws Exception {
        CommandTransformation commandTransformation = new InclusionTransformation();
        TransformationService transformationService = new CommandTransformationService(commandTransformation);
        NotificationService notificationService = new ServerSentNotificationService();

        DocumentService documentService = new MemoryDocumentService(transformationService, notificationService);
        DocumentDto document = documentService.create();

        documentService.applyCommand(1, DtoHelper.insertCommandDto(0 ,0,"12345678", 1));
        documentService.applyCommand(1, DtoHelper.deleteCommandDto(1 ,0,2, 1));
        documentService.applyCommand(1, DtoHelper.deleteCommandDto(1 ,4,2, 1));

        System.out.println(documentService.get(1).content);
    }
}
