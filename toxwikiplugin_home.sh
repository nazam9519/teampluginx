#!/usr/local/bin/bash

set -e

BASE_DIR="$(pwd)"
SOURCE_DIR="$(pwd)/xip"
XWIKI_DIR="$HOME/xwiki2"
STAGING_DIR="$XWIKI_DIR/xips"
UNZIP_DIR="$STAGING_DIR/bbmacsdir"
DEST_DIR="$XWIKI_DIR/xwikidata/data/extension/repository"

rm -rf "$SOURCE_DIR"
mkdir "$SOURCE_DIR"
find "$BASE_DIR" -path "*/target/*.xip" | while read xip; do
  filename=$(basename "$xip")
  echo "Copying $filename from $xip to $SOURCE_DIR/$filename"
  cp "$xip" "$SOURCE_DIR/$filename"
done
# ── 1. Copy all .xip files to staging and rename to .zip ──────────────────────
for file in "$SOURCE_DIR"/*.xip; do
    cp "$file" "$STAGING_DIR/$(basename "${file%.xip}").zip"
done

# ── 2. Unzip all .zip files ────────────────────────────────────────────────────
for zip in "$STAGING_DIR"/*.zip; do
    rm -rf "$UNZIP_DIR"
    unzip -o "$zip" -d "$UNZIP_DIR"

    # ── 3. cp -rn: copy contents, no overwrite ────────────────────────────────
    set +e
    cp -rn "$UNZIP_DIR/." "$DEST_DIR/"
    echo $?
done

cd $XWIKI_DIR
docker-compose down
docker-compose up -d
