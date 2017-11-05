
//example enum of different attacks we can perform
public enum Attack {
    SUCCESS_KEY_VALUE_CHECK("Successful key-value check"),
    FAIL_KEY_VALUE_CHECK("Unsuccessful key-value check");

    private String verbose;

    Attack(String s){
        this.verbose = s;
    }
    @Override
    public String toString(){
        return this.verbose + "\n";
    }

}
