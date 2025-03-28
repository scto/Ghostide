package lsp4custom.com.ninjacoder.customhtmllsp;

import java.util.List;
import io.github.rosemoe.sora.data.CompletionItem;

public class KeyWordConst extends Css3Server {

  String[] fontCss = {
    "Arial",
    "Arial Black",
    "Arial Narrow",
    "Avant Garde",
    "Book Antiqua",
    "Bookman",
    "Calibri",
    "Cambria",
    "Candara",
    "Comic Sans MS",
    "Courier New",
    "Georgia",
    "Impact",
    "Lucida Console",
    "Lucida Grande",
    "Lucida Sans Unicode",
    "Microsoft Sans Serif",
    "Palatino Linotype",
    "Segoe UI",
    "Tahoma",
    "Times New Roman",
    "Trebuchet MS",
    "Verdana",
    "Webdings",
    "Wingdings",
    "Helvetica",
    "Optima",
    "Arial Rounded MT Bold",
    "Baskerville",
    "Bodoni MT",
    "Calisto MT",
    "Century",
    "Century Gothic",
    "Garamond",
    "Papyrus",
    "Rockwell",
    "Algerian",
    "Baskerville Old Face",
    "Perpetua",
    "Symbol",
    "Arial Unicode MS",
    "Comic Sans",
    "Frank Ruhl Libre",
    "DejaVu Sans",
    "Verdana Pro",
    "Futura",
    "Gill Sans",
    "Josefin Sans",
    "Lobster",
    "Merriweather",
    "Montserrat",
    "Noto Sans",
    "Raleway",
    "Arial Nova",
    "Avenir",
    "Bodoni 72",
    "Century Schoolbook",
    "Copperplate",
    "Didot",
    "Elephant",
    "Goudy Old Style",
    "Hoefler Text",
    "Lucida Handwriting",
    "Minion Pro",
    "Optima Nova",
    "Palatino",
    "Perpetua Titling MT",
    "Rockwell Condensed",
    "Segoe Print",
    "Segoe Script",
    "Serifa",
    "Tahoma Narrow",
    "Trajan Pro",
    "Trebuchet",
    "Veranda",
    "Bodoni MT Poster Compressed",
    "Courier",
    "FangSong",
    "SimHei",
    "Simsun",
    "Microsoft YaHei",
    "Noto Serif",
    "Source Sans Pro",
    "Crimson Text",
    "Russo One",
    "Ubuntu",
    "Abril Fatface",
    "Dancing Script",
    "Open Sans",
    "Raleway Dots",
    "Roboto",
    "Shadows Into Light",
    "Oswald",
    "Poppins",
    "Playfair Display",
    "Quicksand",
    "Rubik",
    "Satisfy",
    "Zilla Slab",
    "Almendra",
    "Amatic SC",
    "Archivo Black",
    "Arvo",
    "Bai Jamjuree",
    "Baloo",
    "Barlow",
    "Bitter",
    "Bungee",
    "Calsa",
    "Candal",
    "Caveat",
    "Chivo",
    "Cinzel",
    "Coda",
    "Dosis",
    "Droid Sans",
    "EB Garamond",
    "Exo",
    "Fjalla One",
    "Francois One",
    "Fredoka One",
    "Gloock",
    "Gloria Hallelujah",
    "Gordon",
    "Grand Hotel",
    "Gravitas One",
    "Heebo",
    "Hind",
    "Indie Flower",
    "Inika",
    "Julius Sans One",
    "Just Me Again Down Here",
    "Kalam",
    "Karla",
    "Khand",
    "Knewave",
    "Laila",
    "Lato",
    "Limelight",
    "Lucida Calligraphy",
    "Lusitana",
    "M plus 1p",
    "Maven Pro",
    "Mochiy Pop P One",
    "Montserrat Alternates",
    "Noto Sans JP",
    "Noto Serif JP",
    "Nunito",
    "Nunito Sans",
    "Old Standard TT",
    "Oregano",
    "Oswald Stencil",
    "Overpass",
    "Pathway Gothic One",
    "Pattaya",
    "Raleway Thin",
    "Red Hat Display",
    "Reenie Beanie",
    "Righteous",
    "Roboto Mono",
    "Roboto Slab",
    "Rokkitt",
    "Saira",
    "Salsa",
    "Sarina",
    "Satisfy",
    "Spectral",
    "Spicy Rice",
    "Staatliches",
    "Suez One",
    "Titillium Web",
    "Trirong",
    "Ubuntu Condensed",
    "Varela Round",
    "Varela",
    "Volkhov",
    "Yeseva One",
    "Dancing Script",
    "Overlock",
    "Noto Sans Display",
    "Sanchez",
    "Fira Sans",
    "Pangolin",
    "Noto Sans Hebrew",
    "Prompt",
    "Space Mono",
    "Amiri",
    "Barlow Condensed",
    "Karla",
    "Krypton",
    "Red Hat Text",
    "Secular One",
    "Sofia",
    "Source Serif Pro",
    "Soho",
    "Cooper Black",
    "Droid Serif",
    "Gloock",
    "Inconsolata",
    "Newsreader",
    "Poppins",
    "Sitka",
    "Work Sans",
    "Zaftig",
    "Alegreya",
    "Barlow Semi Condensed",
    "Diplomata",
    "Figtree",
    "Jazz",
    "Mochiy Pop P One",
    "Noto Sans Thai",
    "Open Sans Condensed",
    "Sora",
    "Special Elite",
    "Titillium",
    "Vibur",
    "Zilla Slab Highlight",
    "Alegreya Sans",
    "Exo 2",
    "Play",
    "Rajdhani",
    "Varela Round",
    "Zilla Slab Text"
  };

  public void setLspCustom(
      List<CompletionItem> list,
      String prefix,
      String propName,
      String symbol,
      String[] args,
      String desc,
      String aloritm) {
    var propNames = "";
    if (prefix.startsWith(propNames + symbol)) {
      propNames = propName;
    }
    if (!propNames.isEmpty()) {
      var item = prefix.substring(propNames.length() + 1).trim();
      for (var it : args) {
        if (it.startsWith(item)) {
          list.add(css(it, desc, aloritm));
        }
      }
    }
  }

  public void cssFont(List<CompletionItem> list, String prefix) {

    String propertyName = "";
    if (prefix.startsWith("font-family:")) propertyName = "font-family";

    if (!propertyName.isEmpty()) {
      String colorPrefix = prefix.substring(propertyName.length() + 1).trim();
      for (String color : fontCss) {
        if (color.startsWith(colorPrefix)) {
          list.add(css(color, "cssFont", propertyName + ": " + "\"" + color + "\"" + " ;"));
        }
      }
    }
  }
}
