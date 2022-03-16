
with open("fxml_tags.txt", "w") as file:
    for row in range(0, 8):
        for col in range(0, 8):
            file.write('<Pane fx:id="tile{}{}" prefHeight="98.0" prefWidth="98.0" />\n'.format(row, col))


