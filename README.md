# guntherrotsch.github.io

Blog project with JBake that uses Freemarker templates and Bootstrap structured
for personal Github pages.

The sources and the distribution files are separated in the branches
`sources-master` and `master`. The easiest way to work with this separation
is to setup two git worktrees in one repository, one worktree for the sources
and another worktree for the distribution file. The following sequence of
commands sets up the work space:

```
$ git clone https://github.com/GuntherRotsch/guntherrotsch.github.io.git

$ cd guntherrotsch.github.io.git

$ git checkout sources-master

$ git worktree add dist master
```

The `dist` directory in the project's root is `.gitignore`d in `sources-master`
branch, but it contains the distribution files hosted in `master` branch.

To check the setup:

```
$ git worktree list
/home/gunther/_work/repos/GuntherRotsch.github.com/guntherrotsch.github.io       ee98001 [sources-master]
/home/gunther/_work/repos/GuntherRotsch.github.com/guntherrotsch.github.io/dist  cad00f7 [master]
$
$ git branch -a
  master
* sources-master
  remotes/origin/HEAD -> origin/master
  remotes/origin/master
  remotes/origin/sources-master
$
$ cd dist
$ git branch -a
* master
  sources-master
  remotes/origin/HEAD -> origin/master
  remotes/origin/master
  remotes/origin/sources-master
$
```
