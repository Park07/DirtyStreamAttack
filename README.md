**Replicating Android's DirtyStream Attack Vulnerability**

Based on the research by the Microsoft Group:
https://www.microsoft.com/en-us/security/blog/2024/05/01/dirty-stream-attack-discovering-and-mitigating-a-common-vulnerability-pattern-in-android-apps/

They have discovered that the file sharing mechanism 'Intent' can serve as duplicated roles to read/write and elevate privileges for malicious actors to gain control of your phone.In this demo, I have done:
- File Read
- Settings Hijacking
- File Write/Remote Control Execution (RCE) using the Android NK
(RCE is still very much WIP. The latest update (June 2025) RCE hasn't executed properly. If anyone can help so we can all learn that would be nice :D)

... 
- and Social Engineering

  The Social Engineering is perhpas the least obvious one as I have built an educational tool for people who appreciate arts. It provides helpful description of what each artwork signifies
  and how to appreciate it better. The catch is the if the user clicks 'share' button, it unknowingly performs the 4 attacks mentioned above.

  Languages used:
  -  Kotlin
  -  C++

 NOTE:
 - RCE is still very much WIP. The latest update (June 2025) RCE hasn't executed properly.

**Why This Remains Relevant:**
Despite being discovered in 2024, this vulnerability persists in current Android versions because it stems from developer configuration errors in FileProvider setup, not platform security flaws. Many apps still use overly permissive FileProvider configurations.
