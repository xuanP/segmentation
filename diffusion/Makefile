JFLAGS = -g
JC = javac
.SUFFIXES: .java .class
.java.class:
	$(JC) $(JFLAGS) $*.java

CLASSES = \
	tool.java \
	tukey.java \
	main_diffusion.java

default: classes

classes: $(CLASSES:.java=.class)

clean:
	$(RM) *.class

