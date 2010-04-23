This is release 1d of my Fungus Funge-98 test suite

To get started, load 'fungi' or 'fungy' into a Funge-98 interpreter configured as follows:

* Two dimensional fungespace, preferably infinite size. If infinite size is not possible, make sure it is at least 70 units down and as wide as possible (At least 3 hundred units should do)
* Support for file access

The interpreter must support at least part of the y instruction, and it should preferably cause a reflection on any unimplemented instruction.

If all goes well, the tests should run and a report file should be generated. If not then something has gone wrong, and with a bit of luck you should be given an error code (A number from 0 to 19). To see what it means, check the list at the bottom of the notes file. If no code is given then you may be able to track down the problem to a specific module.

If you're really stuck, then you can load each module in turn into a Befunge interpreter (Most of them will even work in Befunge-93) and save the output from each. If you look at each module (The list is given in the 'modules' file) then you'll see a box of text. Minimum requirements will be displayed in the top right of this box - at the moment it's either !UNE (i.e. not unefunge) or BE (Befunge only). Note that some modules assume that some basic Funge-98 instructions are available (such as a-f); these will still work correctly on (correct) Befunge-93 interpreters, but will cause problems (i.e. crash or lock-up) with Funge-98 ones that implement the complex instructions but not the simple ones.

Note that it is normal for the tests to take a long time, since an 80x25 block of fungespace must be cleared inbetween loading each module. There are still a lot of modules to be added; see the lists in the notes file for more information.

If some of the modules fail then modules run after them may also fail; this is since failed modules may not leave an empty stack on exit.


Interpreter             Known problems
===========             ==============

!Befunge V0.89          None. Not surprising, since Fungus is what I've been
'JBEF' 0x4A424546       using to test it.

FBBI V0.98a             None. There were problems with 0.98a, but they have been
'FBBI' 0x46424249       fixed in the 2003.0722 and 2003.0726 patches. At the
Chris Pressey           moment catseye.mb.ca is down, but presumably when it's
+patch 2003.0722        back up a new version of FBBI incorporating the patches
+patch 2003.0726        will be available. If you need a copy of them before
                        then, then just ask me for them.

ZFunge 0.7.2 beta       You need to configure the fungespace to be 230x60. This
'ZFGE' 0x90707169       allows all the code and the report file to just fit into
Martin Bays             the default size restrictions. However both fungi and
                        fungy fail with error code 3 (The l instruction is
                        passed over instead of causing a reflection), so you
                        need to edit the 'l' to an '<'. However the code will
                        still fail, because the 'i' instruction returns a size
                        equal to the fungespace size instead of the file size.
                        Loading each module manually highlights the following
                        problems:

                        fspace
                                The write to (-2,-2) works, but the fungespace
                                isn't unlimited. Instead the coordinate has
                                been wrapped around, which goes against the spec
                                The fungespace cell size returns some gibberish,
                                because it comes out as a negative number. This
                                is presumably a bug in the long integer code
                                ZFunge employs (Plus a bug in Fungus for not
                                handling negative numbers for all situations)
                        math
                                You are asked for the division by zero and
                                remainder by zero results, when the spec says
                                they should automatically return zero. Also
                                -1%-2 returns 1 when it should be -1, judging by
                                C math.
                        f98space
                                x doesn't work; it should reflect if it is
                                unimplemented.
                        sem
                                All of A-Z fail to reflect, even though they are
                                unimplemented. Even though ( is also meant to
                                be unimplemented, it fails to reflect. ) does
                                the same.
                        stackstack
                                The program says that y says that a new stack
                                hasn't been made. In reality the stack has been
                                made, but only part of y has been implemented in
                                ZFunge so the y query fails.
                        finghrti
                                This produces useless information, due to the
                                problems uncovered by the 'sem' module.
                                

                        Most of these problems are detailed in the ZFunge help
                        text.

RC/Funge-98 1.05        Like ZFunge, RC/Funge-98 has some issues with passing
'RCSU' 0x52425355       over the 'l' instruction instead of causing a reflection
Mike Riley              Replacing the l with a < allows the init code to load.
                        Fungus highlights the following when RC/Funge is run
Patched by Jesse van    with the '-md -Y' options:
Herk (The original
author's website has    string
vanished, so this is            Befunge-93 spaces in strings
the only version I've   f98
been able to find)              3210k$ is 1
                                "v^"1ksv goes straight across
                                4321k$ is 3
                                These are all symptoms of a non-conformant k
                                implementation. In particular, k skips over the
                                instruction it is next to, so performs k-1
                                iterations, and misses the direction change
                                control in "v^"1ksv
                        sem
                                Due to lack of the NULL fingerprint, this test
                                only reports that A-Z reflect. However after
                                writing a mini-funge NULL fingerprint it
                                reports that "NULL"4("AMOR"4("LLUN"4)I pushes 1,
                                (which is incorrect), and then promptly crashes
                                at the end of the test. The crash could be due
                                to my hasty port of the interpreter (hacked out
                                the windowing, curses & signal code) rather than
                                any real bugs though.
                        stackstack
                                The module says that y claims { doesn't create
                                a stack. This is probably due to the non-
                                standard 'y' code (even though -Y had been used
                                to disable it during the test).
                        finghrti
                                T appears to return either garbage or the
                                current time, as opposed to the time since the
                                last M. S was reflected.

                        For some reason the last module always fails to load,
                        usually causing a crash of the interpreter. When it was
                        coaxed into exiting cleanly, the last report was always
                        shown as a single form feed as opposed to anything
                        useful.
                        The only other thing I've noticed about the interpreter
                        is that it expects a newline to follow a form feed in
                        trefunge files.

zfunge 0.0.2pre3+SOCK   Running fungy prints a 0 on screen, returns a warning
'zF98' 0x7A463938       from the interpreter about 'l', loads the init code and
Zinx Verituse           prints the banner message, then seems to lock up.
                        Running fungi does the same.
                        Loading each module individually highlights the
                        following:

                        f98
                                3210k$ is 1
                                "v^"1ksv goes straight across
                                4321k$ is 3
                                These are all symptoms of a non-conformant k
                                implementation. In particular, k skips over the
                                instruction it is next to, so performs k-1
                                iterations, and misses the direction change
                                control in "v^"1ksv
                        f98space
                                00w goes right (should be straight)
                                01w goes right (should be left)
                                10w goes left (should be right)
                        sem
                                Having installed a fungelib version of ROMA, it
                                reports that "LLUN"4("AMOR"4("LLUN"4)I pushes 1.
                        stackstack
                                y says { does not create stack

befc V3.00              Both fungy and fungi exit with error code 15, becase the
'BEFC' 0x42454643       complier doesn't support self-modifying code (i.e.
Jeffrey Lee             loading a file then trying to run the code that was in  
                        it). Compiling and running each module in turn (using
                        the flags '-x 1 -j 4') reveals the following:
                        
                        string
                                Char 0x00 in string returns original char
                                - This is because the compiler treats strings
                                as constants, and so does not pay attention to
                                any modifications performed on them (which is
                                what the module does, in order to insert the
                                null character without causing problems loading
                                the file in some interpreters)
                        f98
                                "v^"1ksv straight across
                                This is because of the compilation method used;
                                there is no real instruction pointer in compiled
                                programs, so iterating over something like s
                                won't cause extra instructions to be skipped

Changelog
---------

Release 1d:

* Fixed a bug in the sem module which prevented ") reflects" message from being displayed if loading the NULL fingerprint fails
* Fixed a bug in the sem module where the ROMA fingerprint wasn't getting unloaded

Release 1c:

* Fixed a bug in the string module that would cause an infinite loop if an interpreter uses Befunge-93 stringmode.

Release 1b:

* Updated the RC/Funge-98 and zfunge results to the latest version of fungus. (Not that anything has changed, mind)
* Removed the '; in string' test from the string module, since f98space already performed that check

Release 1a:

* Corrected the ZFunge results; the string module had been malfunctioning because I'd been running it in Trefunge, not Befunge

Release 1:

* Fixed a bug in concur where the 'IP id unknown' path went would go somewhere nasty
* Fixed a typo in concur where I'd used a non-breaking space instead of a normal one, causing display problems with some interpreters/editors
* Fixed various output bugs in stackstack - thanks to Martin Bays for spotting these last 3 bugs!
* Fixed a bug in string so that 'read-only' strings are detected
* Fixed a bug in string where a newline wasn't being printed at the end of the output. However if any direction change instructions are discovered to work from inside a string, the newline won't be printed (Too much hassle to reshuffle the entire file :p)
* Added a check to string for ; in strings
* Decided to move fungus up to a proper release instead of another preliminary one, since I'm unlikely to be suddenly adding a load of new modules or rewriting the system
* Updated the results for !Befunge to V0.89 and befc to V3.00. Also updated the results for ZFunge (A new version of which, under the name of ZedFunge, is rumoured to be in the works)
* ... However the results for RC/Funge-98 and zfunge haven't been updated since I don't have any copies of them handy at the moment

Preliminary release 4:

* Fixed a bug in f98 which caused improper output after a ' malfunction
* Fixed a bug in b93 which would (probably) cause a crash if ` failed on a befunge-93 interpreter
* Added the comments block to finghrti, and made it unload HRTI on exit
* Added results for the mod_c98/b2c98.h befunge-98 output module of the befc 2 compiler
* Fixed a bug in f98space so full output is produced if j does nothing
* Fixed concur so that output is produced if t does nothing whatsoever

Preliminary release 3:

* Updated results for FBBI, now that the bugs have been fixed
* Updated ZFunge information. 230x60 appears to be the maximum size the fungespace will go to.
* Updated size information at the start of the file: Now minimum of 300x70 needed

Preliminary release 2:

* Fixed a spelling mistake in f98. Backward typing, y'know.
* Fixed another mistake in f98: it was printing "v^"ksv instead of "v^"1ksv
* Added notes for RC/Funge-98 and zfunge
* Added finghrti module

Jeffrey Lee, 17/03/07
me@phlamethrower.co.uk
http://www.quote-egnufeb-quote-greaterthan-colon-hash-comma-underscore-at.info/
