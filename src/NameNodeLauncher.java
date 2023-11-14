import api.NameNode;
import api.NameNodeHelper;
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

public class NameNodeLauncher {
    public static void main(String[] args) {
        try {
            Properties props = new Properties();
            props.put("org.omg.CORBA.ORBInitialHost", "127.0.0.1");
            props.put("org.omg.CORBA.ORBInitialPort", "1050");

            ORB orb = ORB.init(args, props);

            POA rootpoa = POAHelper.narrow(orb.resolve_initial_references("RootPOA"));
            rootpoa.the_POAManager().activate();

            NameNodeImpl nameNodeServant = new NameNodeImpl();

            org.omg.CORBA.Object ref = rootpoa.servant_to_reference(nameNodeServant);
            NameNode href = NameNodeHelper.narrow(ref);

            org.omg.CORBA.Object objRef = orb.resolve_initial_references("NameService");
            org.omg.CosNaming.NamingContextExt ncRef = org.omg.CosNaming.NamingContextExtHelper.narrow(objRef);

            NameComponent[] path = ncRef.to_name("NameNode");
            ncRef.rebind(path, href);

            System.out.println("NameNode is ready and waiting ...");

            orb.run();


        } catch (InvalidName | AdapterInactive | WrongPolicy | ServantNotActive |
                 org.omg.CosNaming.NamingContextPackage.InvalidName | NotFound | CannotProceed e) {
            e.printStackTrace();
        }
    }
}
