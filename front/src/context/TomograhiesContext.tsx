import { useContext, useState } from 'react';
import { TomographiesContext } from './TomographiesContext';

const TomographyFilterComponent = () => {
  const { getTomographies, setDocumentFilter, currentPage } = useContext(TomographiesContext);
  const [documentInput, setDocumentInput] = useState(""); // Temporary local state for the input

  const handleFilterChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setDocumentInput(e.target.value); // Update the local state
  };

  const handleSearch = () => {
    setDocumentFilter(documentInput); // Set the document filter in context
    getTomographies(currentPage); // Trigger the search with the filter
  };

  return (
    <div>
      <input 
        type="text" 
        placeholder="Enter document number" 
        value={documentInput}
        onChange={handleFilterChange} 
      />
      <button onClick={handleSearch}>Search</button>
    </div>
  );
};
