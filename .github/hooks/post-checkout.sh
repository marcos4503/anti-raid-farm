#!/bin/sh

# This script is automatically executed by the integrated Git Bash, and run
# when the current active Branch is changed in this Repository, locally on this
# machine. If you edit this, create a copy of this, without a extension and
# save in this path, with the name of "post-checkout".

echo "===================================================="
echo "Git Hook: Cleaning files informed on '.gitignore'..."
echo "===================================================="

# Delete everything that is listed on ".gitignore" (builds, caches, trash, etc)
git clean -fdX

# Forces Git to delete empty folders that was remained on repository, when the
# currently active Branch was changed...
git clean -fd