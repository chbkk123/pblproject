package nicarng.selcar.Fragment;

public class Dbboard {
    String idx;
    String subject;
    String editor;

    public String getIdx(){return idx;}
    public void setIdx(String idx){this.idx = idx;}

    public String getSubject() {
        return subject;
    }
    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getEditor() {
        return editor;
    }
    public void setEditor(String editor) {
        this.editor = editor;
    }

    public Dbboard(String idx, String subject,String editor){
        this.idx = idx;
        this.subject = subject;
        this.editor = editor;
    }

}
