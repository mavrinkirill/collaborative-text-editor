import interfaces.DocumentService;
import interfaces.NotificationService;
import interfaces.TransformationFactory;
import models.DtoHelper;
import models.document.DocumentDto;
import services.InclusionTransformationFactory;
import services.MemoryDocumentService;
import services.ServerSentNotificationService;
import transformations.*;

import java.util.ArrayList;

public class Program {
    public static void main(String[] args) throws Exception {
        ArrayList<CommandTransformation> transformations = new ArrayList<>();
        transformations.add(new InsertAfterInsertTransformation());
        transformations.add(new InsertAfterDeleteTransformation());
        transformations.add(new DeleteAfterInsertTransformation());
        transformations.add(new DeleteAfterDeleteTransformation());

        TransformationFactory transformationFactory = new InclusionTransformationFactory(transformations);
        NotificationService notificationService = new ServerSentNotificationService();

        DocumentService documentService = new MemoryDocumentService(transformationFactory, notificationService);
        DocumentDto document = documentService.create();

        documentService.applyCommand(1, DtoHelper.insertCommandDto(0 ,0,"1234", 1));
        documentService.applyCommand(1, DtoHelper.insertCommandDto(0 ,0,"5678", 1));
        documentService.applyCommand(1, DtoHelper.deleteCommandDto(1 ,0,2, 1));
        //documentService.applyCommand(1, DtoHelper.deleteCommandDto(1 ,4,2, 1));

        System.out.println(documentService.get(1).content);
    }
}
