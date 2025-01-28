import React, { useEffect, useState } from "react";
import { extensionId } from "./Constants";
import { attach } from "@adobe/uix-guest";
import { Provider, View, defaultTheme } from "@adobe/react-spectrum";
import { ComboBox, Item } from "@adobe/react-spectrum";
import "./CustomDropdown.css";


const CustomDropDown = (props) => {
  const [connection, setConnection] = useState(null);
  const [validationState, setValidationState] = useState(null);
  const [value, setValue] = useState(null);
  const [model, setModel] = useState(null);
  const [items, setItems] = useState(null);

  // Fetch items from an external service
  async function getItems() {
    try {
      const response = await fetch("https://jsonplaceholder.typicode.com/posts?_limit=25"); // Replace with your API URL
      if (!response.ok) {
        throw new Error("Failed to fetch items");
      }
      const data = await response.json();
      return data.map((item) => ({
        id: item.id.toString(), // Ensure id is a string
        name: item.title, // Map title to name
      }));
    } catch (error) {
      console.error("Error fetching items:", error);
      return [{ id: "error", name: "Error loading options" }]; // Fallback option in case of failure
    }
  }

  const onSelectionChange = async (key) => {
    setValue(key); // Update the React state with the selected value
    connection.host.field.onChange(key); // Sync with Content Fragment Editor
  };

  const onOpenChange = async () => {
      const comboBoxHeight = 40; // Approximate height of the ComboBox field
      const optionsHeight = items.length * 32 + 12; // Height of the dropdown options
      const iframeHeight = Math.min(comboBoxHeight + optionsHeight, 400); // Limit height to 400px
      await connection.host.field.setHeight(iframeHeight);
   };

  useEffect(() => {
    const init = async () => {
      // Connect to the Content Fragment Editor
      const conn = await attach({ id: extensionId });
      setConnection(conn);

      // Fetch the Content Fragment Model
      const model = await conn.host.field.getModel();
      setModel(model);

      // Get validation state from the Content Fragment Editor
      conn.host.field.onValidationStateChange((state) => {
        setValidationState(state); // State can be `valid` or `invalid`
      });

      // Get the default value from the Content Fragment Editor
      const defaultValue = await conn.host.field.getDefaultValue();
      setValue(defaultValue);

      // Fetch the list of items for the ComboBox
      const fetchedItems = await getItems();
      setItems(fetchedItems);
    };

    init().catch(console.error);
  }, []);

  // Render a loading state if the component is not yet initialized
  if (!connection || !model || !items) {
    return <Provider theme={defaultTheme}>Loading custom field...</Provider>;
  }

  return (
    <Provider theme={defaultTheme}>
      <View width="100%">
	  
		<div className={"customdropdown-field-wrapper"}>
			<ComboBox
			  label={model.fieldLabel}
			  isRequired={model.required}
			  placeholder={model.emptyText || "Select an option"}
			  errorMessage={model.customErrorMsg}
			  selectedKey={value} // Bind the selected value
			  items={items} // Pass the list of items
			  isInvalid={validationState === "invalid"} // Apply validation state
			  onSelectionChange={onSelectionChange} // Update selection
			  onOpenChange={onOpenChange} // Adjust iframe height dynamically
			  width={"99%"} // Adjust width as needed
			>
			  {(item) => <Item key={item.id}>{item.name}</Item>}
			</ComboBox>
		</div>
      </View>
    </Provider>
  );
};

export default CustomDropDown;
