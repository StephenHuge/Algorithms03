import edu.princeton.cs.algs4.In;

public class SuperTestClient {
    public static void main(String[] args) {
        In in = new In(args[0]);

        String name;
        while((name = in.readLine()) != null) {
            TestClient.main(new String[]{"src/" + name});
        }
    }
}
