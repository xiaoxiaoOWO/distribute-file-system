package api;


/**
* api/_DataNodeStub.java .
* ��IDL-to-Java ������ (����ֲ), �汾 "3.2"����
* ��api.idl
* 2023��11��14�� ���ڶ� ����01ʱ37��36�� CST
*/

public class _DataNodeStub extends org.omg.CORBA.portable.ObjectImpl implements api.DataNode
{

  public byte[] read (int block_id)
  {
            org.omg.CORBA.portable.InputStream $in = null;
            try {
                org.omg.CORBA.portable.OutputStream $out = _request ("read", true);
                $out.write_long (block_id);
                $in = _invoke ($out);
                byte $result[] = api.byteArrayHelper.read ($in);
                return $result;
            } catch (org.omg.CORBA.portable.ApplicationException $ex) {
                $in = $ex.getInputStream ();
                String _id = $ex.getId ();
                throw new org.omg.CORBA.MARSHAL (_id);
            } catch (org.omg.CORBA.portable.RemarshalException $rm) {
                return read (block_id        );
            } finally {
                _releaseReply ($in);
            }
  } // read

  public void append (int block_id, byte[] bytes)
  {
            org.omg.CORBA.portable.InputStream $in = null;
            try {
                org.omg.CORBA.portable.OutputStream $out = _request ("append", true);
                $out.write_long (block_id);
                api.byteArrayHelper.write ($out, bytes);
                $in = _invoke ($out);
                return;
            } catch (org.omg.CORBA.portable.ApplicationException $ex) {
                $in = $ex.getInputStream ();
                String _id = $ex.getId ();
                throw new org.omg.CORBA.MARSHAL (_id);
            } catch (org.omg.CORBA.portable.RemarshalException $rm) {
                append (block_id, bytes        );
            } finally {
                _releaseReply ($in);
            }
  } // append

  public int randomBlockId ()
  {
            org.omg.CORBA.portable.InputStream $in = null;
            try {
                org.omg.CORBA.portable.OutputStream $out = _request ("randomBlockId", true);
                $in = _invoke ($out);
                int $result = $in.read_long ();
                return $result;
            } catch (org.omg.CORBA.portable.ApplicationException $ex) {
                $in = $ex.getInputStream ();
                String _id = $ex.getId ();
                throw new org.omg.CORBA.MARSHAL (_id);
            } catch (org.omg.CORBA.portable.RemarshalException $rm) {
                return randomBlockId (        );
            } finally {
                _releaseReply ($in);
            }
  } // randomBlockId

  // Type-specific CORBA::Object operations
  private static String[] __ids = {
    "IDL:api/DataNode:1.0"};

  public String[] _ids ()
  {
    return (String[])__ids.clone ();
  }

  private void readObject (java.io.ObjectInputStream s) throws java.io.IOException
  {
     String str = s.readUTF ();
     com.sun.corba.se.impl.orbutil.IORCheckImpl.check(str, "api._DataNodeStub");
     String[] args = null;
     java.util.Properties props = null;
     org.omg.CORBA.ORB orb = org.omg.CORBA.ORB.init (args, props);
   try {
     org.omg.CORBA.Object obj = orb.string_to_object (str);
     org.omg.CORBA.portable.Delegate delegate = ((org.omg.CORBA.portable.ObjectImpl) obj)._get_delegate ();
     _set_delegate (delegate);
   } finally {
     orb.destroy() ;
   }
  }

  private void writeObject (java.io.ObjectOutputStream s) throws java.io.IOException
  {
     String[] args = null;
     java.util.Properties props = null;
     org.omg.CORBA.ORB orb = org.omg.CORBA.ORB.init (args, props);
   try {
     String str = orb.object_to_string (this);
     s.writeUTF (str);
   } finally {
     orb.destroy() ;
   }
  }
} // class _DataNodeStub
