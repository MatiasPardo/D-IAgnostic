import { TomographiesContext } from './TomographiesContext'
import { UseTomographies } from '../hooks/UseTomographies'

export const TomographiesProvider = ({ children }) => {
    const { tomographies, getTomographies } = UseTomographies();

    return (
        <TomographiesContext.Provider 
        value={{ 
            tomographies, 
            getTomographies 
        }}>
            {children}
        </TomographiesContext.Provider>
    );
};