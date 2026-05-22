package dev.midnightcoder;

import javax.swing.*;
import java.awt.*;

/**
 * @author Glabay | Glabay-Studios
 * @project MidnightDatabase
 * @social Discord: Glabay
 * @since 2026-05-21
 */
public class Window extends JFrame {
    private final DatabaseManager JDBCDatabaseManager = new JDBCDatabaseManager();
    private final DefaultListModel<Product> products = new DefaultListModel<>();
    private final JList<Product> productList = new JList<>(products);

    private final JTextField nameField = new JTextField();
    private final JTextField priceField = new JTextField();
    private final JTextField quantityField = new JTextField();

    private final JButton addButton = new JButton("Add Product");
    private final JButton removeButton = new JButton("Remove Product");

    static void main() {
        new Window().setVisible(true);
    }

    public Window() {
        setTitle("Midnight Database");
        setSize(400, 420);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        setLayout(new BorderLayout());

        // Initial demo info
        loadProductList();
        initListeners();

        productList.setCellRenderer(createCellRenderer());

        add(createLeftPanel(new JScrollPane(productList)), BorderLayout.WEST);
        add(createRightPanel(), BorderLayout.CENTER);
        setVisible(true);
    }

    private DefaultListCellRenderer createCellRenderer() {
        return new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list,
                                                          Object value,
                                                          int index,
                                                          boolean isSelected,
                                                          boolean cellHasFocus
            ) {
                var component = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Product product)
                    setText("%s (x%s)".formatted(product.name(), product.quantity()));
                return component;
            }
        };
    }

    private JPanel createLeftPanel(JScrollPane scrollPane) {
        var leftPanel = new JPanel(new BorderLayout());
            leftPanel.setPreferredSize(new Dimension(getWidth() / 3, getHeight()));
            leftPanel.setBorder(BorderFactory.createTitledBorder("Products"));
            leftPanel.add(scrollPane, BorderLayout.CENTER);
        return leftPanel;
    }

    private JPanel createRightPanel() {
        var rightPane = new JPanel();
            rightPane.setBorder(BorderFactory.createTitledBorder("Product Details"));
            rightPane.setLayout(new GridLayout(8, 1, 5, 5));
            rightPane.add(new JLabel("Product Name:"));
            rightPane.add(nameField);
            rightPane.add(new JLabel("Price:"));
            rightPane.add(priceField);
            rightPane.add(new JLabel("Quantity:"));
            rightPane.add(quantityField);
            rightPane.add(addButton);
            rightPane.add(removeButton);
        return rightPane;
    }

    private void loadProductList() {
        var allProducts = JDBCDatabaseManager.getAllProducts();
        for (var product : allProducts) {
            nameField.setText(product.name());
            priceField.setText(String.valueOf(product.price()));
            quantityField.setText(String.valueOf(product.quantity()));
            products.addElement(product);
        }
    }

    private void reset() {
        nameField.setText("");
        priceField.setText("");
        quantityField.setText("");
    }

    private void initListeners() {
        addButton.addActionListener(_ -> {
            var product = new Product(
                nameField.getText(),
                Double.parseDouble(priceField.getText()),
                Integer.parseInt(quantityField.getText())
            );
            JDBCDatabaseManager.saveProduct(product);
            products.addElement(product);

            reset();
            JOptionPane.showMessageDialog(this, "Product added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        });

        removeButton.addActionListener(_ -> {
            var selectedProduct = productList.getSelectedValue();
            JDBCDatabaseManager.removeProduct(selectedProduct);
            products.removeElement(selectedProduct);

            reset();
            JOptionPane.showMessageDialog(this, "Product removed successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        });

        productList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                var selectedProduct = productList.getSelectedValue();
                if (selectedProduct != null) {
                    nameField.setText(selectedProduct.name());
                    priceField.setText(String.valueOf(selectedProduct.price()));
                    quantityField.setText(String.valueOf(selectedProduct.quantity()));
                }
            }
        });
    }
}
