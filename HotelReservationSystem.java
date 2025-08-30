import java.sql.*;
import java.util.Scanner;

public class HotelReservationSystem {
   private static final String url = "jdbc:mysql://localhost:3306/hotel_booking";
   private static final String username = "root";
   private static final String password = "Password007";


    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
        }catch(ClassNotFoundException e){
            System.out.println(e.getMessage());
        }

        try{
            Connection con= DriverManager.getConnection(url,username,password);
            while(true){
                System.out.println();
                System.out.println("WELCOME TO OUR HOTEL");
                Scanner sc = new Scanner(System.in);
                System.out.println("Option 1: Reserve a room");
                System.out.println("Option 2: View reservation ");
                System.out.println("Option 3: Get Room Number");
                System.out.println("Option 4: Update Reservations ");
                System.out.println("Option 5: Delete Reservations");
                System.out.println("Option 0: Exit ");
                System.out.println("Choose an option ");
                int choose = sc.nextInt();
                switch (choose) {
                    case 1:
                        reservation(con, sc);
                        break;
                    case 2:
                        viewReservations(con);
                        break;
                    case 3:
                        getRoomnumber(con, sc);
                        break;
                    case 4:
                        updateReservation(con, sc);
                        break;
                    case 5:
                        deleteReservation(con, sc);
                        break;
                    case 0:
                        exit();
                        sc.close();
                        return;
                    default:
                        System.out.println("Invalid choice. Try again.");
                }
            }

        }catch (Exception e){
            System.out.println(e.getMessage());
        }

    }

    private static void reservation(Connection con, Scanner sc ){

               try {
                   System.out.println("Enter guest name: ");
                   String guest = sc.next();

                   System.out.println("Enter room number: ");
                   int Room_no = sc.nextInt();

                   System.out.println("Enter contact number: ");
                   String contact = sc.next();
                   String sql = "INSERT INTO reservations (guest_name, room_number, contact_number) " +
                           "VALUES ('" + guest + "', " +Room_no + ", '" + contact + "')";

                   try (Statement stmt = con.createStatement()) {

                       int rowsaffected = stmt.executeUpdate(sql);
                       if (rowsaffected > 0) {
                           System.out.println("Reservation Successfull ");
                       } else {
                           System.out.println("Reservation failed ");
                       }
                   }
               }
            catch(SQLException e){
            e.printStackTrace();

        }

    }

    private static void viewReservations(Connection con) throws SQLException{
        String sql = "SELECT reservation_id, Guest_name, Room_number, Contact_Number, Reservation_date FROM reservations";
        try(Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sql)){
            System.out.println("Current reservations");
            System.out.println("+----------------+-----------------+---------------+----------------------+-------------------------+");
            System.out.println("| Reservation ID | Guest           | Room Number   | Contact Number      | Reservation Date        |");
            System.out.println("+----------------+-----------------+---------------+----------------------+-------------------------+");
            while(rs.next()){
                int reservation_id = rs.getInt("reservation_id");
                String guest_name = rs.getString("Guest_name");
                int room_number = rs.getInt("Room_number");
                String contact_number = rs.getString("Contact_Number");
                String reservation_date = rs.getTimestamp("Reservation_date").toString();

                System.out.printf("| %-14d | %-15s | %-13d | %-20s | %-19s   |\n",
                        reservation_id, guest_name,room_number,contact_number,reservation_date);


            }
            System.out.println("+----------------+-----------------+---------------+----------------------+-------------------------+");

        }
    }

    private static void getRoomnumber(Connection con , Scanner sc){
        try{
            System.out.println("Enter reservation id: ");
            int reservation_id = sc.nextInt();
            System.out.println("Enter guest name");
            String guestname = sc.next();
            String sql = "SELECT room_number FROM reservations " +
                    "WHERE reservation_id = " + reservation_id +
                    " AND Guest_name = '" + guestname + "'";

            try(Statement stmt = con.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {
                if (rs.next()) {
                    int roomNumber = rs.getInt("Room_number");
                    System.out.println("Room number for reservation id " + reservation_id + "And guest " + guestname + "is" + roomNumber);

                } else {
                    System.out.println("Reservation not found ");
                }
            }
        }catch(SQLException e){
            e.printStackTrace();

        }

    }

    private static void updateReservation(Connection con, Scanner sc) {
        try {
            System.out.println("Enter reservation ID to update");
            int reservationId = sc.nextInt();
            sc.nextLine();

            if(!reservationExists(con,reservationId)){
                System.out.println("reservation not found for the given id ");
                return;
            }
            System.out.println("Enter guest name: ");
            String guest = sc.next();

            System.out.println("Enter room number: ");
            int Room_no = sc.nextInt();

            System.out.println("Enter contact number: ");
            String contact = sc.next();

            String sql = "UPDATE reservations SET Guest_name = '" + guest + "', " +
                    "Room_number = " + Room_no + ", " +
                    "Contact_Number = '" + contact+ "' " +
                    "WHERE reservation_id = " + reservationId;
             try(Statement stmt = con.createStatement()){
                 int affectedrows = stmt.executeUpdate(sql);

                 if(affectedrows > 0){
                     System.out.println("reservation updated");
                 }
                 else{
                     System.out.println("reservation failed");
                 }
             }


        }catch(SQLException e){
            e.printStackTrace();
        }
    }
    private static void deleteReservation(Connection connection, Scanner scanner) {
        try {
            System.out.print("Enter reservation ID to delete: ");
            int reservationId = scanner.nextInt();

            if (!reservationExists(connection, reservationId)) {
                System.out.println("Reservation not found for the given ID.");
                return;
            }

            String sql = "DELETE FROM reservations WHERE reservation_id = " + reservationId;

            try (Statement statement = connection.createStatement()) {
                int affectedRows = statement.executeUpdate(sql);

                if (affectedRows > 0) {
                    System.out.println("Reservation deleted successfully!");
                } else {
                    System.out.println("Reservation deletion failed.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static boolean reservationExists(Connection connection, int reservationId) {
        try {
            String sql = "SELECT reservation_id FROM reservations WHERE reservation_id = " + reservationId;

            try (Statement statement = connection.createStatement();
                 ResultSet resultSet = statement.executeQuery(sql)) {

                return resultSet.next();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    public static void exit() throws InterruptedException {
        System.out.print("Exiting ");
        int i = 5;
        while(i!=0){
            System.out.print(".");
            Thread.sleep(1000);
            i--;
        }
        System.out.println();
        System.out.println("ThankYou ");
    }
}



