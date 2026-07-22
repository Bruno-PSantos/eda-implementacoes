import java.util.ArrayList;
import java.util.Arrays;

public class TestPV {

    public static void main(String[] args) {
        TestPV t = new TestPV();

        t.testAdd();
        t.testLinearAdd();
        t.testZigZagLeftInsertion();
        t.testZigZagRightInsertion();
        t.testSearch();

        System.out.println("PASSOU NOS TESTES!!!");

    }

    public void testAdd() {
        PV pv = new PV();

        pv.add(10);
        assert pv.isEmpty() == false;
        assert pv.size() == 1;
        assert pv.blackHeight() == 1;

        pv.add(5);
        pv.add(15);

        assert pv.size() == 3;
        assert pv.blackHeight() != -1;

        ArrayList<Integer> v = new ArrayList<>(Arrays.asList(10, 5, 15));
        assert pv.bfs().equals(v);
    }

    public void testLinearAdd() {
        PV pv = new PV();

        pv.add(1);
        pv.add(2);
        pv.add(3);
        pv.add(4);
        pv.add(5);
        pv.add(6);
        pv.add(7);

        assert pv.size() == 7;
        assert pv.blackHeight() == 2;
        
    }

    public void testZigZagLeftInsertion() {
        PV pv = new PV();
        
        pv.add(30);
        pv.add(10);
        pv.add(20);

        ArrayList<Integer> v = new ArrayList<>(Arrays.asList(20, 10, 30));
        assert pv.bfs().equals(v);
        assert pv.blackHeight() == 1;
    }

    public void testZigZagRightInsertion() {
        PV pv = new PV();

        pv.add(10);
        pv.add(30);
        pv.add(20);

        ArrayList<Integer> v = new ArrayList<>(Arrays.asList(20, 10, 30));
        assert pv.bfs().equals(v);
        assert pv.blackHeight() == 1;
    }

    public void testSearch() {
        PV pv = new PV();

        pv.add(10);
        pv.add(20);
        pv.add(30);

        assert pv.search(20) != null;
        assert pv.search(20).value == 20;
        
        assert pv.search(999) == pv.search(888);
        assert pv.search(999) == pv.search(5);
        assert pv.search(999) == pv.search(15);
    }

}