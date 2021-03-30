package de.thkoeln.intermodulationdemo.util;

//import com.sun.javafx.css.converters.SizeConverter;
import javafx.beans.DefaultProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.property.StringPropertyBase;
import javafx.css.CssMetaData;
import javafx.css.Styleable;
import javafx.css.StyleableFloatProperty;
import javafx.css.StyleableProperty;
import javafx.css.converter.SizeConverter;
import javafx.geometry.NodeOrientation;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.text.Font;
import org.jfree.fx.FXGraphics2D;
import org.scilab.forge.jlatexmath.ParseException;
import org.scilab.forge.jlatexmath.TeXConstants;
import org.scilab.forge.jlatexmath.TeXFormula;
import org.scilab.forge.jlatexmath.TeXIcon;

import de.thkoeln.intermodulationdemo.App;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.FileSystemAlreadyExistsException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.BiPredicate;

/**
 * The {@code LatexViewFixed} is a JavaFX {@code Node} used for rendering LaTeX formulas
 *
 * @author Egor Makarenko
 */
@DefaultProperty("formula")
public class LatexViewFixed extends Canvas {

    /**
     * Font size to use by default.
     */
    private static final float DEFAULT_SIZE = (float) Font.getDefault().getSize();

    /**
     * LaTeX formula to use by default.
     */
    private static final String DEFAULT_FORMULA = "";

    static {
        // Load all available TTF fonts from JLatexMath packages
        BiPredicate<Path, BasicFileAttributes> isFont = (path, attr) -> path.toString().endsWith(".ttf");
        String[] packages = {
                "fonts/fonts/",
                "fonts/cyrillic/fonts/",
                "fonts/greek/fonts/"
        };
        for (String pkg : packages) {
            try {
                URI uri = App.class.getResource(pkg).toURI();
                Path path;
                if (uri.getScheme().equals("jar")) {
                    try {
                        path = Paths.get((new File(System.getProperty("user.dir")+"/"+pkg)).toURI());
                    } catch (FileSystemAlreadyExistsException e) {
                    	path = FileSystems.getFileSystem(uri).getPath(pkg);
                    }
                } else {
                    path = Paths.get(uri);
                }
                
                Files.find(path, 5, isFont)
                .map(Path::toString)
                .forEach(font -> {
					try {
						Font.loadFont(new FileInputStream(new File(font)), -1);
					} catch (FileNotFoundException e) {
						System.err.println("FileNotFound " + font);
						e.printStackTrace();
					}
				});
               
            } catch (URISyntaxException | IOException e) {
                System.err.println("Failed to load fonts from package " + pkg);
            }
        }
    }

    private TeXIcon texIcon;

    /**
     * Allocates a new {@code LatexViewFixed} object.
     */
    public LatexViewFixed() {
        getStyleClass().add(DEFAULT_STYLE_CLASS);
        setNodeOrientation(NodeOrientation.LEFT_TO_RIGHT);
    }

    /**
     * Allocates a new {@code LatexViewFixed} object using the given LaTeX formula.
     *
     * @param formula LaTeX formula that this LatexViewFixed uses
     */
    public LatexViewFixed(String formula) {
        getStyleClass().add(DEFAULT_STYLE_CLASS);
        setNodeOrientation(NodeOrientation.LEFT_TO_RIGHT);
        formulaProperty().set(formula);
    }

    private void update() throws ParseException {
        TeXFormula teXFormula;
        try {
            teXFormula = new TeXFormula(getFormula());
        } catch (ParseException e) {
            System.err.println(e.getMessage());
            teXFormula = new TeXFormula("\\textcolor{red}{\\text{Error}}");
        }

        texIcon = teXFormula.createTeXIcon(TeXConstants.STYLE_DISPLAY, getSize());

        GraphicsContext gc = getGraphicsContext2D();
        gc.clearRect(0, 0, getWidth(), getHeight());

        double width = texIcon.getIconWidth();
        double height = texIcon.getIconHeight();

        setWidth(width);
        setHeight(height);

        gc.clearRect(0, 0, width, height);

        FXGraphics2D graphics = new FXGraphics2D(gc);
        texIcon.paintIcon(null, graphics, 0, 0);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isResizable() {
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double prefWidth(double height) {
        return texIcon.getIconWidth();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double prefHeight(double width) {
        return texIcon.getIconHeight();
    }

    /**
     * Formula text
     *
     * @return formula text as a {@code StringProperty}
     */
    public final StringProperty formulaProperty() {
        if (formula == null) {
            formula = new StringPropertyBase(DEFAULT_FORMULA) {
                @Override
                public void invalidated() {
                    LatexViewFixed.this.update();
                }

                @Override
                public Object getBean() {
                    return LatexViewFixed.this;
                }

                @Override
                public String getName() {
                    return "formula";
                }
            };
        }
        return formula;
    }

    private StringProperty formula;

    public final void setFormula(String value) {
        formulaProperty().set(value == null ? DEFAULT_FORMULA : value);
    }

    public final String getFormula() {
        String value = formulaProperty().get();
        return value == null ? DEFAULT_FORMULA : value;
    }

    /**
     * Formula text size
     *
     * @return formula text size as a {@code FloatProperty}
     */
    public final StyleableFloatProperty sizeProperty() {
        if (size == null) {
            size = new StyleableFloatProperty(DEFAULT_SIZE) {
                @Override
                public void invalidated() {
                    LatexViewFixed.this.update();
                }

                @Override
                public Object getBean() {
                    return LatexViewFixed.this;
                }

                @Override
                public String getName() {
                    return "size";
                }

                @Override
                public CssMetaData<LatexViewFixed, Number> getCssMetaData() {
                    return LatexViewFixed.StyleableProperties.SIZE;
                }
            };
        }
        return size;
    }

    private StyleableFloatProperty size;

    public final void setSize(float value) {
        sizeProperty().set(value);
    }

    public final float getSize() {
        return sizeProperty().get();
    }

    /***************************************************************************
     * Stylesheet Handling
     **************************************************************************/

    private static final String DEFAULT_STYLE_CLASS = "latex-view";

    /**
     * Super-lazy instantiation pattern from Bill Pugh.
     * @treatAsPrivate implementation detail
     */
    private static class StyleableProperties {
        private static final CssMetaData<LatexViewFixed,Number> SIZE =
                new CssMetaData<LatexViewFixed,Number>("-fx-font-size", SizeConverter.getInstance(), DEFAULT_SIZE) {

                    @Override
                    public boolean isSettable(LatexViewFixed node) {
                        return node.size == null || !node.size.isBound();
                    }

                    @Override
                    public StyleableProperty<Number> getStyleableProperty(LatexViewFixed node) {
                        return node.sizeProperty();
                    }
                };

        private static final List<CssMetaData<? extends Styleable, ?>> STYLEABLES;

        static {
            final List<CssMetaData<? extends Styleable, ?>> styleables =
                    new ArrayList<>();
            styleables.add(SIZE);
            STYLEABLES = Collections.unmodifiableList(styleables);
        }
    }

    /**
     * @return The CssMetaData associated with this class, which may include the
     * CssMetaData of its super classes.
     */
    public static List<CssMetaData<? extends Styleable, ?>> getClassCssMetaData() {
        return StyleableProperties.STYLEABLES;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<CssMetaData<? extends Styleable, ?>> getCssMetaData() {
        return getClassCssMetaData();
    }
}