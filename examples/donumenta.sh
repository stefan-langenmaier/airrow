#!/bin/bash

# replace the creator with the secret id

# black tower
curl -X POST \
  -F 'meta={
    "creator":"289ae107-aa5f-4423-9b5d-487e449ece56",
    "location":{"lat":49.02404,"lon":12.09730},
    "accuracy":10,
    "artistName":"Klára Orosz",
    "artistCountry":"Hungary",
    "artistPlace":"Pécs",
    "artistIntroDe":"Klára Orosz lebt und arbeitet in Pécs, Ungarn. Die promovierte Künstlerin studierte an der dortigen Universität und am Goldsmiths College in London. Heute lehrt sie an den Universitäten Pécs und Szeged. Ihre bildhauerische Praxis wandelte sich von Skulpturen zu groß dimensionierten Installationen im Stadtraum, die mit Passanten in Interaktion treten.",
    "artistIntroEn":"Klára Orosz lives and works in Pécs, Hungary. The artist, studied at the University of Pécs and the Goldsmiths College in London. She is teaching at the University of Pécs and the University of Szeged. Her sculptural practice changed from sculptures to large-scale installations in urban spaces that interact with passers-by.",
    "artistLink":"http://klaraorosz.hu",
    "objectName":"The Black Tower",
    "objectYear":"2019",
    "objectPlace": "93047 Regensburg",
    "objectStreet": "Am Brückenbasar 1",
    "objectIdeaDe":"Der Entwurf einer Neuinterpretation des nicht mehr erhaltenen Schwarzen Turms an der Nordseite der Steinernen Brücke. Der Turm war Bestandteil der Grenzbefestigung und Zollstation von Regensburg und wurde 1810 nach der Beschießung durch napoleonische und österreichische Truppen abgetragen. 2002 stieß man auf die Grundmauern des Gebäudes und konnte daraus seine genaue Position, Länge und Breite ableiten. Klára Orosz arbeitet mit den Originalmaßen des schwarzen Turms von 20 m Höhe. Dieser Entwurf konnte leider nicht realisiert werden.",
    "objectIdeaEn":"The design of a new interpretation of the Black Tower on the north side of the Stone Bridge, formed part of the border fortifications and customs station of Regensburg in the Stadtamhof district. The tower was removed in 1810 after being damaged by Napoleonic and Austrian troops. In 2002, the foundation walls of the building were revealed and allowed its exact positioning, length and width. Klára Orosz worked with the original dimensions of the black tower with 20 m height. Unfortunately, this design could not be realized.",
    "objectLink":"https://www.donumenta.de/ueber-uns/aktuell-artikel/news/neue-blicke-von-aussen-auf-das-weltkulturerbe-regensburg/",
    "previewMediaName":"preview.jpg",
    "backgroundMediaName":"background.jpg",
    "objectMediaName":"bt.glb"}' \
  -F "objectMedia=@donumenta/bt/bt.glb" \
  -F "previewMedia=@donumenta/bt/preview.jpg" \
  -F "backgroundMedia=@donumenta/bt/background.jpg" \
  https://api.vr.donumenta.de/upload && echo "ok turm"

# mbb
curl -X POST \
  -F 'meta={
    "creator":"289ae107-aa5f-4423-9b5d-487e449ece56",
    "location":{"lat":49.02136,"lon":12.10189},
    "accuracy":10,
    "artistName":"Dušan Zahoranský",
    "artistCountry":"Czech Republic",
    "artistPlace":"Prag",
    "artistIntroDe":"Dušan Zahoranský lebt und arbeitet als bildender Künstler, Dozent und freier Kurator in Prag. Er schloss die Academy of Fine Arts und Design in Bratislava, Slowakei als Master of the Arts ab und arbeitete als Dozent und freier Kurator. Aktuell lehrt er als Vizerektor an der Akademie der Bildenden Künste in Prag.",
    "artistIntroEn":"Dušan Zahoranský lives and works in Prague as a visual artist - lecturer and freelance curator. He studied with the degree of Masters of Arts at the Academy of Fine Arts and Design in Bratislava, Slovakia. Currently he teaches as vice-rector at the Academy of Fine Arts in Prague.",
    "artistLink":"https://www.artlist.cz/en/dusan-zahoransky-2675/",
    "objectName":"MICHAEL BUSCHHEUER BRÜCKE",
    "objectYear":"2020",
    "objectPlace": "93047 Regensburg",
    "objectStreet": "Eiserne Brücke",
    "objectIdeaDe":"Mit SEARCH and RESCUE wurde eine symbolische Umwidmung öffentlicher Plätze vollzogen. So gelangen die Helden unserer Zeit, die sich der Humanität verschrieben haben, Sea-Eye Gründer Michael Buschheuer (MICHAEL BUSCHHEUER BRÜCKE), aber auch die Opfer Alan und Ghalib Kurdi (ALAN & GHALIB KURDI HAFEN), die für die vielen stehen, die wir nicht retten konnten in den Fokus.",
    "objectIdeaEn":"With SEARCH and RESCUE, a symbolic rededication of public places were accomplished. In this way, the heroes of our time who have dedicated themselves to humanity, Sea-Eye founder Michael Buschheuer (MICHAEL BUSCHHEUER BRÜCKE), but also the victims Alan and Ghalib Kurdi (ALAN & GHALIB KURDI HAFEN), who stand for the many we were unable to save, come into focus.",
    "objectLink":"https://www.donumenta.de/air-interventionen/dusan-zahoransky-search-and-rescue/",
    "previewMediaName":"preview.jpg",
    "backgroundMediaName":"background.jpg",
    "objectMediaName":"mbb.glb"}' \
  -F "objectMedia=@donumenta/mbb/mbb.glb" \
  -F "previewMedia=@donumenta/mbb/preview.jpg" \
  -F "backgroundMedia=@donumenta/mbb/background.jpg" \
  https://api.vr.donumenta.de/upload && echo "ok mbb"

# akh
curl -X POST \
  -F 'meta={
    "creator":"289ae107-aa5f-4423-9b5d-487e449ece56",
    "location":{"lat":49.02005,"lon":12.10398},
    "accuracy":10,
    "artistName":"Dušan Zahoranský",
    "artistCountry":"Czech Republic",
    "artistPlace":"Prag",
    "artistIntroDe":"Dušan Zahoranský lebt und arbeitet als bildender Künstler, Dozent und freier Kurator in Prag. Er schloss die Academy of Fine Arts und Design in Bratislava, Slowakei als Master of the Arts ab und arbeitete als Dozent und freier Kurator. Aktuell lehrt er als Vizerektor an der Akademie der Bildenden Künste in Prag.",
    "artistIntroEn":"Dušan Zahoranský lives and works in Prague as a visual artist - lecturer and freelance curator. He studied with the degree of Masters of Arts at the Academy of Fine Arts and Design in Bratislava, Slovakia. Currently he teaches as vice-rector at the Academy of Fine Arts in Prague.",
    "artistLink":"https://www.artlist.cz/en/dusan-zahoransky-2675/",
    "objectName":"ALAN & GHALIB KURDI HAFEN",
    "objectYear":"2020",
    "objectPlace": "93047 Regensburg",
    "objectStreet": "Eiserne Brücke",
    "objectIdeaDe":"Mit SEARCH and RESCUE wurde eine symbolische Umwidmung öffentlicher Plätze vollzogen. So gelangen die Helden unserer Zeit, die sich der Humanität verschrieben haben, Sea-Eye Gründer Michael Buschheuer (MICHAEL BUSCHHEUER BRÜCKE), aber auch die Opfer Alan und Ghalib Kurdi (ALAN & GHALIB KURDI HAFEN), die für die vielen stehen, die wir nicht retten konnten in den Fokus.",
    "objectIdeaEn":"With SEARCH and RESCUE, a symbolic rededication of public places were accomplished. In this way, the heroes of our time who have dedicated themselves to humanity, Sea-Eye founder Michael Buschheuer (MICHAEL BUSCHHEUER BRÜCKE), but also the victims Alan and Ghalib Kurdi (ALAN & GHALIB KURDI HAFEN), who stand for the many we were unable to save, come into focus.",
    "objectLink":"https://www.donumenta.de/air-interventionen/dusan-zahoransky-search-and-rescue/",
    "previewMediaName":"preview.jpg",
    "backgroundMediaName":"background.jpg",
    "objectMediaName":"akh.glb"}' \
  -F "objectMedia=@donumenta/akh/akh.glb" \
  -F "previewMedia=@donumenta/akh/preview.jpg" \
  -F "backgroundMedia=@donumenta/akh/background.jpg" \
  https://api.vr.donumenta.de/upload && echo "ok akh"

# parabelle
curl -X POST \
  -F 'meta={
    "creator":"289ae107-aa5f-4423-9b5d-487e449ece56",
    "location":{"lat":49.01398,"lon":12.09933},
    "accuracy":10,
    "artistName":"Notburga Karl",
    "artistCountry":"Germany",
    "artistPlace":"Nürnberg",
    "artistIntroDe":"Notburga Karl studierte an der Akademie der Bildenden Künste in München und der Kunstakademie Düsseldorf. Sie war Meisterschülerin von Jannis Kounellis und studierte an der SVA (School of Visual Arts) in New York. Derzeit lehrt sie Kunst an der Otto-Friedrich-Universität in Bamberg. Ihre Performances, Skulpturen, Mobiles, Video- und Klanginstallationen schaffen eine Resonanz zum gegebenen Ort.",
    "artistIntroEn":"Notburga Karl studied at the Academy of Fine Arts in Munich and the Düsseldorf Art Academy. She was a master student of Jannis Kounellis and she studied at the SVA (School of Visual Arts) in New York. She currently teaches art at the Otto-Friedrich University in Bamberg. Her performances, sculptures, mobiles, video and sound installations create a resonance to the given site.",
    "artistLink":"https://www.uni-bamberg.de/kunstdidaktik/team/notburga-karl/",
    "objectName":"Parabelle",
    "objectYear":"2021",
    "objectPlace": "93047 Regensburg",
    "objectStreet": "Keplerdenkmal",
    "objectIdeaDe":"Der Monopteros ist eine ideale Kreisform in der Architektur und trifft bei Kepler auf einen Widerspruch, denn der Astronom Kepler hat die elliptischen Planetenbahnen entdeckt. Die Ellipse „Parabelle“ ist hier das zentrale Element und man könnte sie als Korrektur oder Kommentar des runden Denkmals bezeichnen. Für die Künstlerin ist die Ellipse ein Kreis, der Fahrt aufgenommen hat. Sie ist eine Metapher für ein Denken in Bewegung, ein Umdenken. Es geht nicht nur um die Planetenbahnen, sondern auch um ein Weltbild, das auf den Kopf gestellt wurde. Kepler ist dafür ein gutes Beispiel.",
    "objectIdeaEn":"The monopteros is an ideal circular shape in architecture and here it meets a strange contradiction in Kepler because the astronomer discovered the elliptical planetary orbits. The ellipse „Parabelle“ is the central element in the artwork and could be called a correction or commentary of the round monument. For the artist, the ellipse is a circle that has gained momentum. It is a metaphor for thinking in motion, is a rethinking. It is not only about planetary orbits, but also about a worldview that has been turned upside down. Kepler could be seen as a good example of this idea.",
    "objectLink":"https://www.donumenta.de/art-lab-gleis-1/kuenstlerinnen/notburga-karl-k-wie/",
    "previewMediaName":"preview.jpg",
    "backgroundMediaName":"background.jpg",
    "objectMediaName":"pb.glb"}' \
  -F "objectMedia=@donumenta/pb/pb.glb" \
  -F "previewMedia=@donumenta/pb/preview.jpg" \
  -F "backgroundMedia=@donumenta/pb/background.jpg" \
  https://api.vr.donumenta.de/upload && echo "ok pb"

# all my rivers
curl -X POST \
  -F 'meta={
    "creator":"289ae107-aa5f-4423-9b5d-487e449ece56",
    "location":{"lat":49.02215,"lon":12.09782},
    "accuracy":10,
    "artistName":"Eszter Dalma Muray",
    "artistCountry":"Hungary/England",
    "artistPlace":"Budapest/London",
    "artistIntroDe":"Eszter Dalma Muray lebt und arbeitet in Budapest und London. Die freischaffende Designerin ist auf Print-, Interaktions- und Umweltdesign spezialisiert. Sie studierte Visuelle Kommunikation an der Glasgow School of Art und Interaktionsdesign an der ArtEZ in Arnheim, Niederlande. Neben ihrer Arbeit mit digitalen Programmen bevorzugt sie die Werkstoffe Holz, Beton und Silikon.",
    "artistIntroEn":"Eszter Dalma Muray lives and works in Budapest and London. She is an independent designer specializing in print, interaction and environmental design. She studied visual communication at Glasgow School of Art and interaction design at ArtEZ in Arnhem, the Netherlands. She works with a wide variety of digital programs and materials, such as wood, concrete or silicone.",
    "artistLink":"https://eszter.persona.co/",
    "objectName":"All my rivers",
    "objectYear":"2021",
    "objectPlace": "93047 Regensburg",
    "objectStreet": "Am Beschlächt",
    "objectIdeaDe":"Auf 2850 km zeigt sich die Donau in unterschiedlichster Form, Größe und Fließgeschwindigkeit. Von unbedeutend zu dramatisch wechselt sie zwischen langsamen Mäandern zu schnell fließender Gebärde. Die mobile Skulptur des Flusses verändert sich ständig– je nach Tag, Wetter und Jahreszeit. Zudem ist die Donau Spiegel, die die Ufer und den Himmel über sich reflektiert. Sie verbindet zehn Länder, viele Landschaften und Menschen mit unterschiedlichsten Geschichten.",
    "objectIdeaEn":"On its 2,850km journey the Danube evolves in its shape, size and flow, growing from insignificant to dramatic, oscillating between a slow meander and a harsh rapid. The river behaves like a mobile sculpture, ever changing with the day, season and the weather. It is in fact a mirror in itself, reflecting the shores and the sky above it. It connects several countries, landscapes, and people all with different histories.",
    "objectLink":"https://www.donumenta.de/air-interventionen/eszter-muray-all-my-rivers/",
    "previewMediaName":"preview.jpg",
    "backgroundMediaName":"background.jpg",
    "objectMediaName":"amr.glb"}' \
  -F "objectMedia=@donumenta/amr/amr.glb" \
  -F "previewMedia=@donumenta/amr/preview.jpg" \
  -F "backgroundMedia=@donumenta/amr/background.jpg" \
  https://api.vr.donumenta.de/upload && echo "ok amr"

  # art lab
curl -X POST \
  -F 'meta={
    "creator":"289ae107-aa5f-4423-9b5d-487e449ece56",
    "location":{"lat":49.01203,"lon":12.09925},
    "accuracy":10,
    "artistName":"donumenta ART LAB Gleis 1",
    "artistCountry":"Germany",
    "artistPlace":"Regensburg",
    "artistIntroDe":"Das donumenta ART LAB Gleis 1 befindet sich in der ehemaligen Fußgängerunterführung am Hauptbahnhof in Regensburg. Der 60 Meter lange Tunnel ist Kunst-Labor und Ausstellungsort.",
    "artistIntroEn":"The donumenta ART LAB Gleis 1 is located in the former pedestrian underpass at the railway station in Regensburg. The 60 meter long tunnel is an art laboratory and exhibition space.",
    "artistLink":"https://www.donumenta.de/art-lab-gleis-1/",
    "objectName":"donumenta ART LAB Gleis 1",
    "objectYear":"2021",
    "objectPlace": "93047 Regensburg",
    "objectStreet": "Bahnhofstraße 18",
    "objectIdeaDe":"Zwischen den Polen Wissenschaft und Kunst versteht sich das donumenta ART LAB Gleis 1 als Experimentierfeld. In vier bis fünf Ausstellungen pro Jahr gestalten international renommierte Künstler*innen aus den 14 Ländern des Donauraumes diesen außergewöhnlichen Raum mit ortsspezifischen Installationen.",
    "objectIdeaEn":"Between the poles of science and art, the donumenta ART LAB Gleis 1 sees itself as a field of experimentation. In four to five exhibitions per year, internationally renowned artists from the 14 countries of the Danube region design this extraordinary space with site-specific installations.",
    "objectLink":"https://www.donumenta.de/art-lab-gleis-1/",
    "previewMediaName":"preview.jpg",
    "backgroundMediaName":"background.jpg",
    "objectMediaName":"link.txt"}' \
  -F "objectMedia=@donumenta/al/link.txt" \
  -F "previewMedia=@donumenta/al/preview.jpg" \
  -F "backgroundMedia=@donumenta/al/background.jpg" \
  https://api.vr.donumenta.de/upload && echo "ok al"
