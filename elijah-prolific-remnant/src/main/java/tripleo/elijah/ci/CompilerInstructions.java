package tripleo.elijah.ci;

import antlr.*;

import java.util.*;

public interface CompilerInstructions {

	IndexingStatement indexingStatement();

	void add(GenerateStatement generateStatement);

	void add(LibraryStatementPartImpl libraryStatementPart);

	String getFilename();

	void setFilename(String filename);

	String genLang();

	String getName();

	void setName(String name);

	void setName(Token name);

	List<LibraryStatementPart> getLibraryStatementParts();

}
