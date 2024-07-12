## Radical paradox

`Radical paradox` is:

* a blend of `pr-fluffy-cegont-paf-240626-007` and
  `pr-fluffy-cegont-paf-240709`, with some stuff taken out/history rewritten.

* a testbed for newer tools than raw git and Idea.

* going to be rewritten a couple times before it's really beautiful.

* of the lineage of the above from github.  I'll pull in the kindof stuff later.

* (idk) likely a merge point for the stuff from the other branches as well.



<details>
  <summary>Click me to see the old stuff</summary>

Elijah prolific remnant
========================

Elijah is:

- ... a high-level language suitable for replacement of* Java and C/C++.
- ... a historical curiosity.
- ... is meant to integrate into current C and Java projects.
- ... is free software intended for use on all systems, including GNU/Linux.
- ... is licensed under LGPL.

`prolific-remnant` is:

- ... implemented in Java (17)
- ... uses Maven


Instructions
-------------

[https://github.com/elijah-team/prolific-remnant](https://github.com/elijah-team/prolific-remnant)

```shell
git clone https://github.com/elijah-team/prolific-remnant
mkdir prolific-remnant/COMP
( cd prolific-remnant && nix-shell -p maven jdk17 --pure --command "mvn test")
```


Goals
------

- Serve as a baseline to push back into fluffy
 
- Explore how the fluffy flow works


Lineage
--------

`Fluffy mainline-k`

`Fluffy mainline-k-2023-09`


Stolen from rife2
------------------

## Download the dependencies

```bash
./bld download
```

## Running the server

```bash
./bld compile run
```

Go to:

http://localhost:8080/


## Deploying the app

```bash
./bld war
```

The resulting archive will be in:
`war/build/libs`


## Making an UberJar


```bash
./bld uberjar
```

Then run it with:

```bash
java -jar build/dist/rifers-1.0.0-uber.jar
```

</details>
