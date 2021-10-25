# THB_TicTacToe
Patterns &amp; Frameworks Winter 21

## Students 
Peggy, Iris, Florian, Daniel

## How to get these files?

just clone the url as
git clone https://github.com/florschBf/THB_TicTacToe.git
in your IDE terminal

## Setup of the Development Environment

### AndroidStudio Project

Create the project from the VCS (`File`, `New`, `Project from Version Control`) and build it.

### Emulator

* Ensure Virtualization is enabled in your BIOS settings (can be well-hidden).
* For Windows 10, ensure Hyper-V and WHPX are disabled.
* For AMD CPUs, ensure that in `Tools`, `SDK Manager`, `SDK Tools`, the Android Emulator Hypervisor Driver for AMD Processors is downloaded and installed properly.

----------------------16.10.2021 iris------------------- 

CONFIGURATION
File -> Settings -> System Settings -> 
-> SDK Platforms:
		Android 12.0(S)
		
-> SDK Tools:
		Android SDK Build-Tools 31
		Android Emulator
		Android SDK Platform-Tools
		Intel x86 Emulator Accelerator (HAXM installer)
		
		
AVD Manager (Virtual Devices Emulator - https://developer.android.com/studio/run/managing-avds) 
-> Select a system image:
	Marshmallow -> Download
->  Marshmallow -> Next -> Nexus 5X API 26
						-> Galaxy Nexus API 23 
