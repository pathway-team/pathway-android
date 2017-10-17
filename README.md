# pathway-android
The android application for the Pathway app.

This Android application serves as the main client application for the Pathway fitness app.

# CONTRIBUTING

### FORK (Only do this if you are contributing from outside of the pathway-team organization, otherwise skip to BRANCH)

Fork the project [on GitHub](https://github.com/pathway-team/pathway-android) and check out
your copy.

```sh
$ git clone https://github.com/pathway-team/pathway-android.git
$ cd pathway-android
$ git remote add upstream git://github.com/pathway-team/pathway-android.git
```

Now decide if you want your feature or bug fix to go into the master branch
or the stable branch.  As a rule of thumb, bug fixes go into the stable branch
while new features go into the master branch.

The stable branch is effectively frozen; patches that change the bionode-demo 
run-time behavior get rejected.

bionode-demo has several bundled dependencies that are not part of the project proper.  Any changes to files
in those directories or its subdirectories should be sent to their respective
projects.  Do not send your patch to us, we cannot accept it.

In case of doubt, open an issue in the [issue tracker][] or contact one of the [project maintainers][].

Especially do so if you plan to work on something big.  Nothing is more
frustrating than seeing your hard work go to waste because your vision
does not align with that of a project maintainer.


### BRANCH

Okay, so you have decided on the proper branch.  Create a feature branch
and start hacking:

```sh
$ git checkout -b my-feature-branch
```

### COMMIT

Make sure git knows your name and email address:

```sh
$ git config --global user.name "J. Random User"
$ git config --global user.email "j.random.user@example.com"
```

Writing good commit logs is important.  A commit log should describe what
changed and why.  Follow these guidelines when writing one:

1. The first line should be 50 characters or less and contain a short
   description of the change prefixed with the name of the changed
   subsystem (e.g. "net: add localAddress and localPort to Socket").
2. Keep the second line blank.
3. Wrap all other lines at 72 columns.

A good commit log looks like this:

```
subsystem: explaining the commit in one line

Body of commit message is a few lines of text, explaining things
in more detail, possibly giving some background about the issue
being fixed, etc etc.

The body of the commit message can be several paragraphs, and
please do proper word-wrap and keep columns shorter than about
72 characters or so. That way `git log` will show things
nicely even when it is indented.
```

The header line should be meaningful; it is what other people see when they
run `git shortlog` or `git log --oneline`.

Check the output of `git log --oneline files_that_you_changed` to find out
what subsystem (or subsystems) your changes touch.


### REBASE

Use `git rebase` (not `git merge`) to sync your work from time to time.

```sh
$ git fetch upstream
$ git rebase upstream/v0.01  # or upstream/master
```


### TEST (NEEDS TO BE UPDATE WITH OUR TESTING PROCESS)



### PUSH

```sh
$ git push origin my-feature-branch
```

Go to https://github.com/pathway-team/pathway-android and select your feature branch.  Click
the 'Pull Request' button and fill out the form.

Pull requests are usually reviewed within a few days.  If there are comments
to address, apply your changes in a separate commit and push that to your
feature branch.  Post a comment in the pull request afterwards; GitHub does
not send out notifications when you add commits.

[issue tracker]: https://github.com/pathway-team/pathway-android/issues
[PathWay mailing list]: (TODO: Create mailing list)

~~[project maintainers]: https://github.com/pathway-team/pathway-android/wiki/Contributors~~

This doc forked from [joyent/node](https://github.com/nodejs/node-v0.x-archive/blob/master/CONTRIBUTING.md)
