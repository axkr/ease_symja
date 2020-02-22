# Symja Eclipse EASE plugin
 
 
[Symja computer algebra language](https://github.com/axkr/symja_android_library) integration with [Eclipse EASE - scripting environment](https://www.eclipse.org/ease/) for the [Eclipse platform](https://www.eclipse.org/)
 
 ## Requirements
 
 This plugin requires the installation of 
 - the [Eclipse EASE - scripting environment](https://www.eclipse.org/ease/)
 - the [Eclipse Nebula - Supplemental Custom Widgets](https://www.eclipse.org/nebula/) especially the [Nebula NatTable - high performance SWT data grid](https://www.eclipse.org/nattable/)
 
 - an [example Github repository](https://github.com/axkr/symja_examples) contains some Symja example scripts.
 
 ## Usage
 
 ### Use Symja as a script shell
 
 ![Symja Script Shell](ease_symja1.png)
 
 ### Editor for Symja files
 
The `*.sym` file extension is mapped to the Symja editor

 ![Symja Editor context menu](ease_symja4.png)
 
The Symja editor supports *content assist* (press `Ctrl+SPACE` after typing the beginning of a function), 
*syntax highlighting* and displaying *function documentation*
 
 ![Symja Editor syntax highlighting](ease_symja2.png)
 
The `*.sym` file extension can also be started as a Symja Script (EASE Script) launch configuration.
The Script runs independent from the *Symja Script Shell* view

 ![Symja launch configuration](ease_symja3.png)
 
In the help pages you can find a *getting started* guide for the Symja language:

 ![Symja Editor with hoover info](ease_symja5.png)