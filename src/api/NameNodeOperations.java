package api;


/**
* api/NameNodeOperations.java .
* ��IDL-to-Java ������ (����ֲ), �汾 "3.2"����
* ��api.idl
* 2023��11��14�� ���ڶ� ����01ʱ37��36�� CST
*/

public interface NameNodeOperations 
{

  // ????"(��?	?
  String open (String filepath, int mode);

  // O????"(��?	?
  void close (String fileInfo);
} // interface NameNodeOperations
