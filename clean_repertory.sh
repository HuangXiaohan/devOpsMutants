echo "#   Cleaning the repertory..."

SCRIPT_PATH="$(cd "$(dirname "$0")" && pwd)" # Répertoire du script


find $SCRIPT_PATH/spoon-mutators -name '*Mutator.java' | while read line; do
	mutator=$(basename ${line})

	rm -rf $SCRIPT_PATH/mutant_${mutator%.java}
	echo "Removed folder 'mutant_${mutator%.java}'"
done

echo "#   Repertory cleaned"
