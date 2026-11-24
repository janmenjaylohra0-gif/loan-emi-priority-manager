import java.io.*;
import java.util.*;
public class LoanEMIManager {

    static class Loan {
        String id, name, status;
        long amount;
        int priority, order;  
        Loan(String id, String name, long amount, int priority, int order, String status) {
            this.id = id; this.name = name; this.amount = amount;
            this.priority = priority; this.order = order; this.status = status;
        }

        public String toString() {
            return id + " | " + name + " | " + amount + " | priority " + priority + " | " + status;
        }
    }

    static class Node {
        Loan loan;
        Node left, right;
        Node(Loan loan) { this.loan = loan; }
    }

    static Node root = null;

    static Node insert(Node node, Loan loan) {
        if (node == null) return new Node(loan);
        if (loan.amount < node.loan.amount) node.left = insert(node.left, loan);
        else node.right = insert(node.right, loan);       
        return node;
    }

    static void inorder(Node node) {                       
        if (node == null) return;
        inorder(node.left);
        System.out.println(node.loan);
        inorder(node.right);
    }

    static PriorityQueue<Loan> queue = new PriorityQueue<>((a, b) ->
            a.priority != b.priority ? b.priority - a.priority : a.order - b.order);

    static HashMap<String, Loan> loans = new HashMap<>();  

    static int nextId = 101;
    static int nextOrder = 1;
    static final String FILE = "loan_applications.txt";

    static void addLoan(Loan loan) {
        loans.put(loan.id, loan);
        root = insert(root, loan);
        if (loan.status.equals("PENDING")) queue.add(loan);
    }

    static void submit(Scanner sc) {
        try {
            System.out.print("Name: ");
            String name = sc.nextLine().trim();
            System.out.print("Loan amount: ");
            long amount = Long.parseLong(sc.nextLine().trim());
            System.out.print("Priority (1-10, higher = more urgent): ");
            int priority = Integer.parseInt(sc.nextLine().trim());

            if (name.isEmpty() || name.contains("|")) {
                System.out.println("Invalid name.");
            } else if (amount <= 0) {
                System.out.println("Loan amount must be positive.");
            } else if (priority < 1 || priority > 10) {
                System.out.println("Priority must be between 1 and 10.");
            } else {
                Loan loan = new Loan("L" + nextId++, name, amount, priority, nextOrder++, "PENDING");
                addLoan(loan);
                System.out.println("Application received: " + loan.id + " | " + loan.name + " | " + loan.amount);
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid number entered.");
        }
    }

    static void search(Scanner sc) {
        System.out.print("Application ID: ");
        String id = sc.nextLine().trim().toUpperCase();
        Loan loan = loans.get(id);                         
        if (loan == null) System.out.println("Application " + id + " not found.");
        else System.out.println(loan);
    }

    static void processNext() {
        Loan loan = queue.poll();                        
        if (loan == null) {
            System.out.println("No pending applications.");
        } else {
            loan.status = "PROCESSED";
            System.out.println("Processing " + loan.id + " | " + loan.name);
        }
    }

    static void display() {
        if (loans.isEmpty()) { System.out.println("No applications."); return; }
        System.out.println("--- All loans by amount (BST in-order) ---");
        inorder(root);
    }

    static void report() {
        if (loans.isEmpty()) { System.out.println("No applications."); return; }
        ArrayList<Loan> list = new ArrayList<>(loans.values());
        for (int i = 1; i < list.size(); i++) {
            Loan key = list.get(i);
            int j = i - 1;
            while (j >= 0 && comesAfter(list.get(j), key)) {
                list.set(j + 1, list.get(j));
                j--;
            }
            list.set(j + 1, key);
        }
        System.out.println("--- Review report (priority, then amount) ---");
        for (Loan l : list) System.out.println(l);
    }

    static boolean comesAfter(Loan a, Loan b) {          
        if (a.priority != b.priority) return a.priority < b.priority;
        if (a.amount != b.amount) return a.amount < b.amount;
        return a.order > b.order;
    }

    static void load() {
        File f = new File(FILE);
        if (!f.exists()) return;
        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty() || line.startsWith("#")) continue;
                try {
                    String[] p = line.split("\\|");
                    Loan loan = new Loan(p[0], p[1], Long.parseLong(p[2]),
                            Integer.parseInt(p[3]), nextOrder++, p[4]);
                    if (loan.amount <= 0 || loans.containsKey(loan.id)) continue;
                    addLoan(loan);
                    nextId = Math.max(nextId, Integer.parseInt(loan.id.substring(1)) + 1);
                } catch (Exception e) {
                    System.out.println("Skipped invalid line: " + line);
                }
            }
        } catch (IOException e) {
            System.out.println("Could not read file.");
        }
    }

    static void save() {
        ArrayList<Loan> list = new ArrayList<>(loans.values());
        list.sort((a, b) -> a.order - b.order);
        try (PrintWriter pw = new PrintWriter(new FileWriter(FILE))) {
            pw.println("# ID|Name|Amount|Priority|Status");
            for (Loan l : list)
                pw.println(l.id + "|" + l.name + "|" + l.amount + "|" + l.priority + "|" + l.status);
            System.out.println("Saved " + list.size() + " application(s).");
        } catch (IOException e) {
            System.out.println("Could not save file.");
        }
    }
    public static void main(String[] args) {
        load();
        Scanner sc = new Scanner(System.in);
        while (true) {
            System.out.println("\n1. Submit application");
            System.out.println("2. Search application");
            System.out.println("3. Process next");
            System.out.println("4. Display all (by amount)");
            System.out.println("5. Sorted report");
            System.out.println("0. Save and exit");
            System.out.print("Choice: ");
            if (!sc.hasNextLine()) break;
            String choice = sc.nextLine().trim();

            if (choice.equals("1")) submit(sc);
            else if (choice.equals("2")) search(sc);
            else if (choice.equals("3")) processNext();
            else if (choice.equals("4")) display();
            else if (choice.equals("5")) report();
            else if (choice.equals("0")) break;
            else System.out.println("Invalid choice.");
        }
        save();
    }
}
