import interfaces.DocumentService;
import interfaces.TransformationService;
import models.dto.CommandDto;
import models.dto.DocumentDto;
import models.dto.DtoHelper;
import services.CommandTransformationService;
import services.ConcurrentDocumentService;
import transformation.command.CommandTransformation;
import transformation.command.InclusionTransformation;

import java.util.ArrayList;

public class Program {
    public static void main(String[] args) throws Exception {
        CommandTransformation commandTransformation = new InclusionTransformation();
        TransformationService transformationService = new CommandTransformationService(commandTransformation);

        DocumentService documentService = new ConcurrentDocumentService(transformationService);
        DocumentDto document = documentService.create();

        documentService.applyCommand(1, DtoHelper.insertCommandDto(0 ,0,"1234", 1));
        documentService.applyCommand(1, DtoHelper.deleteCommandDto(1 ,0,3, 1));

        System.out.println(documentService.get(1).content);

        ArrayList<CommandDto> history = documentService.getHistory(1, 0);

        /*
        document.command(new InsertCommand(0, 0, "12345", 2));
        System.out.println(document.getContent());

        document.command(new DeleteCommand(1, 1, 1, 2));
        System.out.println(document.getContent());

        document.command(new InsertCommand(1, 1, "ZZ", 2));
        System.out.println(document.getContent());
        */

        /*
        document.command(new InsertCommand(1, 4, "2222", 1));
        System.out.println(document.getContent());
        document.command(new InsertCommand(1, 4, "3333", 1));
        System.out.println(document.getContent());
        document.command(new InsertCommand(0, 0, "4444", 1));
        System.out.println(document.getContent());
         */



        //document.command(new command.string.InsertStringCommand(1, "bbbb"));
        //document.command(new command.string.DeleteStringCommand(0, 4));
        //document.command(new command.string.InsertStringCommand(0, "bbbb"));
    }
}
