// Stub class generated by rmic, do not edit.
// Contents subject to change without notice.

package org.eclipse.persistence.testing.tests.sessionbroker;

public final class RMIServerManagerController_Stub
    extends java.rmi.server.RemoteStub
    implements org.eclipse.persistence.testing.tests.sessionbroker.RMISessionBrokerServerManager, java.rmi.Remote
{
    private static final long serialVersionUID = 2;
    
    private static java.lang.reflect.Method $method_createRemoteSessionController_0;
    
    static {
	try {
	    $method_createRemoteSessionController_0 = org.eclipse.persistence.testing.tests.sessionbroker.RMISessionBrokerServerManager.class.getMethod("createRemoteSessionController", new java.lang.Class[] {});
	} catch (java.lang.NoSuchMethodException e) {
	    throw new java.lang.NoSuchMethodError(
		"stub class initialization failed");
	}
    }
    
    // constructors
    public RMIServerManagerController_Stub(java.rmi.server.RemoteRef ref) {
	super(ref);
    }
    
    // methods from remote interfaces
    
    // implementation of createRemoteSessionController()
    public org.eclipse.persistence.sessions.remote.rmi.RMIRemoteSessionController createRemoteSessionController()
	throws java.rmi.RemoteException
    {
	try {
	    Object $result = ref.invoke(this, $method_createRemoteSessionController_0, null, 1192999235963804723L);
	    return ((org.eclipse.persistence.sessions.remote.rmi.RMIRemoteSessionController) $result);
	} catch (java.lang.RuntimeException e) {
	    throw e;
	} catch (java.rmi.RemoteException e) {
	    throw e;
	} catch (java.lang.Exception e) {
	    throw new java.rmi.UnexpectedException("undeclared checked exception", e);
	}
    }
}
