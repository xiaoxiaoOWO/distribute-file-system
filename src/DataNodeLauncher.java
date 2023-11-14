import api.DataNode;
import api.DataNodeHelper;
import api.NameNode;
import api.NameNodeHelper;
import impl.DataNodeImpl;
import impl.NameNodeImpl;
import org.omg.CORBA.ORB;
import org.omg.CORBA.ORBPackage.InvalidName;
import org.omg.CosNaming.NameComponent;
import org.omg.CosNaming.NamingContextPackage.CannotProceed;
import org.omg.CosNaming.NamingContextPackage.NotFound;
import org.omg.PortableServer.POA;
import org.omg.PortableServer.POAHelper;
import org.omg.PortableServer.POAManagerPackage.AdapterInactive;
import org.omg.PortableServer.POAPackage.ServantNotActive;
import org.omg.PortableServer.POAPackage.WrongPolicy;

import java.util.Properties;
import java.util.Scanner;

public class DataNodeLauncher {
    public static void main(String[] args) {
        try {
            Properties props = new Properties();
            props.put("org.omg.CORBA.ORBInitialHost", "127.0.0.1");
            props.put("org.omg.CORBA.ORBInitialPort", "1050");

            ORB orb = ORB.init(args, props);

            POA rootpoa = POAHelper.narrow(orb.resolve_initial_references("RootPOA"));
            rootpoa.the_POAManager().activate();

            Scanner scanner = new Scanner(System.in);
            System.out.println("Enter DataNode ID:");
            int id = scanner.nextInt();

            DataNodeImpl dataNodeServant = new DataNodeImpl(id);


            org.omg.CORBA.Object ref = rootpoa.servant_to_reference(dataNodeServant);
            DataNode href = DataNodeHelper.narrow(ref);


            org.omg.CORBA.Object objRef = orb.resolve_initial_references("NameService");
            org.omg.CosNaming.NamingContextExt ncRef = org.omg.CosNaming.NamingContextExtHelper.narrow(objRef);

            NameComponent[] path = ncRef.to_name("DataNode"+id);
            ncRef.rebind(path, href);

            System.out.println("DataNode is ready and waiting ...");

            orb.run();


        } catch (InvalidName | AdapterInactive | WrongPolicy | ServantNotActive |
                 org.omg.CosNaming.NamingContextPackage.InvalidName | NotFound | CannotProceed e) {
            e.printStackTrace();
        }
    }
}
