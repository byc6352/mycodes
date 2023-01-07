/*
 * This file is auto-generated.  DO NOT MODIFY.
 * Original file: H:\\works\\android\\eclipse\\workspace\\ClearThunderQq\\src\\com\\byc\\ClearThunderQq\\ProcessConnection.aidl
 */
package com.byc.ClearThunderQq;
public interface ProcessConnection extends android.os.IInterface
{
/** Local-side IPC implementation stub class. */
public static abstract class Stub extends android.os.Binder implements com.byc.ClearThunderQq.ProcessConnection
{
private static final java.lang.String DESCRIPTOR = "com.byc.ClearThunderQq.ProcessConnection";
/** Construct the stub at attach it to the interface. */
public Stub()
{
this.attachInterface(this, DESCRIPTOR);
}
/**
 * Cast an IBinder object into an com.byc.ClearThunderQq.ProcessConnection interface,
 * generating a proxy if needed.
 */
public static com.byc.ClearThunderQq.ProcessConnection asInterface(android.os.IBinder obj)
{
if ((obj==null)) {
return null;
}
android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
if (((iin!=null)&&(iin instanceof com.byc.ClearThunderQq.ProcessConnection))) {
return ((com.byc.ClearThunderQq.ProcessConnection)iin);
}
return new com.byc.ClearThunderQq.ProcessConnection.Stub.Proxy(obj);
}
@Override public android.os.IBinder asBinder()
{
return this;
}
@Override public boolean onTransact(int code, android.os.Parcel data, android.os.Parcel reply, int flags) throws android.os.RemoteException
{
switch (code)
{
case INTERFACE_TRANSACTION:
{
reply.writeString(DESCRIPTOR);
return true;
}
}
return super.onTransact(code, data, reply, flags);
}
private static class Proxy implements com.byc.ClearThunderQq.ProcessConnection
{
private android.os.IBinder mRemote;
Proxy(android.os.IBinder remote)
{
mRemote = remote;
}
@Override public android.os.IBinder asBinder()
{
return mRemote;
}
public java.lang.String getInterfaceDescriptor()
{
return DESCRIPTOR;
}
}
}
}
