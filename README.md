# JavaLib_Common_Imaging
The `JavaLib_Common_*` libraries contains all code snippets, classes and concepts, I want to reuse over more than one project.  
In `JavaLib_Common_Imaging` contains classes for:
* image handling (caching or computing similarities)
* an image reader with transparent hashing support
* an image renderer based on bumpmapping
* a character renderer used by the image renderer above
	* Sorry, current font file is only a very small one with 4 character. A larger one is currently in progress.

`JavaLib_Common_Imaging` is an extract of `JavaLib_Common`.  
At first and for a long time I've used SVN for versioning and all these extracts were made via `SVN externals` (separate files or whole folders).
So all changes in the "extracted" files in the "extract" libraries were also made in the original files of `JavaLib_Common`.
The "extracted" files had the same full history as the original ones.  
Now in GIT there is no similar mechanism like `SVN externals`, as far as I know.
So I've decided to make reduced copies of `JavaLib_Common`.
All files in the "extract" libraries were removed from `JavaLib_Common` and vice versa,
that each file in all these libraries is exclusivly in only one library.  
As a result, `JavaLib_Common` contains the complete history of all files, but only the least used files are left in the current state.
All other files were moved into the "extract" libraries, but without their history.

### Usage / Development
The current state of this library is compatible with JAVA 17. But most of its code should be compatible with JAVA 8.  
This is an Eclipse project. `.classpath` and `.project` files are contained in the repository.  
`JavaLib_Common_Imaging` don't depends on other libraries.
