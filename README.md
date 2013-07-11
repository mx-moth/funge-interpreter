Befunge IDE and interperator
============================

A [Befunge](esolangs.org/wiki/Befunge) IDE.

I first made this years ago, only recently remembering and resurecting it. I
finally fixed a bug that I knew about previously, after 5 years!

Compiling
---------

	javac -d bin/ -cp src/ $( find src/ -name '*.java' )

Running
-------

	java -cp bin/ maelstrom.funge.gui.FungeGui

Compliance to the spec
----------------------

This implements most of the basic [Funge-98 spec][f98-s]. It is missing the
following Funge-98 features:

* Filesystem Funge
* Concurrent Funge
* Other dimensional funges (Unefunge, Trefunge, N-Funge)
* Load and unload semantics `(`/`)`
* System info operator `y`
* Stack stack manipulation `{`, `}`, `u`

Funge-98 is a superset of the [Befunge-93 spec][b93-spec], so Befunge-93
programs should work with one notable exception: Values are not limited to
bytes (0 to 256), but longs (-2⁶³ to 2⁶³-1).

[b93-spec]: http://catseye.tc/node/Befunge-93.html "Befunge-93 spec"
[f98-spec]: http://catseye.tc/projects/funge98/doc/funge98.html#Integers "Funge-98 spec"
