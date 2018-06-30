# ApiCallsWithDeb
ApiCallsWithDeb is a tool written using Java Debug Interface java debugger API from Oracle. ApiCallsWithDeb can be used to get
methodcalls of methods 
defined in the Android App.

## Usage

### Runnning App
1. Find .apk file of the App, the APK must be debuggable.
2. Run `./run.sh APK_FILE` to run the Android App. This will both install and Run App in debg mode to wait for debugger to connect.

### Runnning ApiCallsWithDeb

ApiCallsWithDeb can be launched from both commandline and Netbeans IDE.

### Using Netbeans
1. Download and open project with Netbeans 8.2
2. Add downloaded tool.jar into classpath of project
3. Run project

### Using commandline
1. Run ApiCallsWithDeb using `java -jar ApiCallsWithDeb.jar`
