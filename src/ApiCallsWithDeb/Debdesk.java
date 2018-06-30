
package ApiCallsWithDeb;

import com.sun.jdi.VirtualMachine;
import com.sun.jdi.Bootstrap;
import com.sun.jdi.connect.*;
import java.io.BufferedReader;

import java.util.Map;
import java.util.List;

import java.io.PrintWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Iterator;

/**
 * This program traces the execution of another program.
 * See "java Trace -help".
 * It is a simple example of the use of the Java Debug Interface.
 *
 * @author Robert Field
 */
public class Debdesk{

    // Running remote VM
    private VirtualMachine vm;

    // Thread transferring remote error stream to our error stream
    private Thread errThread = null;

    // Thread transferring remote output stream to our output stream
    private Thread outThread = null;

    // Mode for tracing the Trace program (default= 0 off)
    private int debugTraceMode = 0;
    String args [];
    //  Do we want to watch assignments to fields
    private boolean watchFields = true;

    // Class patterns for which we don't want events
    private String[] excludes = {"java.*", "javax.*", "sun.*",
                                 "com.sun.*","android.*","android.support.*","com.android.*","dalvik.*","libcore.*"};

    /**
     * main
     */
    public static void main(String[] args) {
        new Debdesk(args);
    }

    /**
     * Parse the command line arguments.
     * Launch target VM.
     * Generate the trace.
     */
   Debdesk(String[] args) {
        this.args=args;
        PrintWriter writer = new PrintWriter(System.out);
        /*Connector connector= findLaunchingConnector();
        Map arguments=connector.defaultArguments();      
        System.out.println(arguments.keySet().iterator().next());
        Connector.Argument opts=(Connector.Argument) arguments.get("options");
        Connector.Argument main=(Connector.Argument) arguments.get("main");
        Connector.Argument susp=(Connector.Argument) arguments.get("suspend");        
        susp.setValue("true");
        main.setValue("debdesk.Test");
        opts.setValue("-cp build/classes");
        LaunchingConnector launcher=(LaunchingConnector) connector; */      
        Connector connector= findLaunchingConnector();
        Map arguments=connector.defaultArguments();
        Connector.Argument host=(Connector.Argument) arguments.get("hostname");
        Connector.Argument port=(Connector.Argument) arguments.get("port");
        host.setValue("localhost");
        port.setValue("54322");
        AttachingConnector attacher=(AttachingConnector) connector;
        vm=null;  
        try
        {  
           System.out.println("pppppprerer");
           vm=attacher.attach(arguments);      
           System.out.println("rerer");
           //vm=launcher.launch(arguments);
           generateTrace(writer);           
        }
        catch (Exception e) {  } 
    
    }
    void generateTrace(PrintWriter writer) {
        vm.setDebugTraceMode(debugTraceMode);
        EventThread eventThread = new EventThread(vm, excludes, writer,args[0]);
        eventThread.setEventRequests(watchFields);
        eventThread.start();
        redirectOutput();
        vm.resume();
        try {
            eventThread.join();
            errThread.join(); // Make sure output is forwarded
            outThread.join(); // before we exit
        } catch (InterruptedException exc) {
            // we don't interrupt
        }
        writer.close();
    }
    
    void redirectOutput() {
        Process process = vm.process();

        // Copy target's output and error to our output and error.
        errThread = new StreamRedirectThread("error reader",
                                             process.getErrorStream(),
                                             System.err);
        outThread = new StreamRedirectThread("output reader",
                                             process.getInputStream(),
                                             System.out);
        errThread.start();
        outThread.start();
    }

    /**
     * Find a com.sun.jdi.CommandLineLaunch connector
     */
   AttachingConnector findLaunchingConnector() {
        List<Connector> connectors = Bootstrap.virtualMachineManager().allConnectors();
        for (Connector connector : connectors) {
            if (connector.name().equals("com.sun.jdi.SocketAttach")) {
                return (AttachingConnector)connector;
            }
        }
        throw new Error("No launching connector");
    }
     /*LaunchingConnector findLaunchingConnector() {
        List<Connector> connectors = Bootstrap.virtualMachineManager().allConnectors();
        for (Connector connector : connectors) {
            if (connector.name().equals("com.sun.jdi.CommandLineLaunch")) {
                return (LaunchingConnector)connector;
            }
        }
        throw new Error("No launching connector");
    }*/
}
