#!/bin/bash

# Convert JSP files to HTML
for file in *.jsp; do
    if [ -f "$file" ]; then
        # Create HTML version
        html_file="${file%.jsp}.html"
        # Copy content and remove JSP-specific tags
        sed 's/<%@.*%>//g; s/<jsp:.*>//g; s/<%=.*%>//g' "$file" > "$html_file"
        echo "Converted $file to $html_file"
    fi
done 