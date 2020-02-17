package document;

public class DocumentState {
    public DocumentState(long version, String content){
        this.version = version;
        this.content = content;
    }

    public long version;

    public String content;
}
