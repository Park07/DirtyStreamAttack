**Replicating Android's DirtyStream Attack Vulnerability**

Based on the research by the Microsoft Group:
https://www.microsoft.com/en-us/security/blog/2024/05/01/dirty-stream-attack-discovering-and-mitigating-a-common-vulnerability-pattern-in-android-apps/

They have discovered that the file sharing mechanism 'Intent' can serve as duplicated roles to read/write and elevate privileges for malicious actors to gain control of your phone.In this demo, I have done:
- File Read
- Settings Hijacking
- File Write
- Remote Control Execution (RCE) using the Android NK

... 
- and Social Engineering

  The Social Engineering is perhpas the least obvious one as I have built an educational tool for people who appreciate arts. It provides helpful description of what each artwork signifies
  and how to appreciate it better. The catch is the if the user clicks 'share' button, it unknowingly performs the 4 attacks mentioned above.

  Languages used:
  -  Kotlin
  -  C++
 
  Whilst the research paper outlined what popular apps (WPS Office, Xioami File) were vulnerable and several techniques, I had built the app from the ground up as a proof of concept.
  Even though the findings are over 2 years old, the reason why the issues still persists even in the latest Android v16, is due to it being programmer's error. 
