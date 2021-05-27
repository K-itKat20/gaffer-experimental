import React, {ReactElement} from "react";
import { Box, FormControl, Grid, InputLabel, MenuItem, Select } from "@material-ui/core";
import { StoreType } from "../../domain/store-type";
import {GetStoreTypesRepo} from "../../rest/repositories/get-store-types-repo";

interface IProps {
    value: StoreType;
    onChange(storeType: StoreType): void;
}

export default function StoreTypeSelect(props: IProps): ReactElement {
    
    const { value, onChange } = props;
    const [storeTypes, setStoretypes] = React.useState([]);
    async function getStoreTypes(): Promise<Array<string>> {
        let storeTypes: Array<string>;
        try {
            storeTypes = await new GetStoreTypesRepo().get();
        } catch(e) {
            storeTypes = [];
        }
        return storeTypes;
    }
    return (
            <Grid item xs={12} id={"storetype-select-grid"} aria-label="store-type-grid" >
                <FormControl
                    variant="outlined"
                    id={"storetype-formcontrol"}
                    aria-label="store-type-input"
                    fullWidth
                >
                    <InputLabel aria-label="store-type-input-label" style={{fontSize: "20px"}} htmlFor={"storetype-select"} id={"storetype-select-label"}>Store Type</InputLabel>
                    <Box my={1}/>
                    <Select
                        inputProps={{
                            name: "Store Type",
                            id: "outlined-age-native-simple",
                            "aria-label": "store-type-input"
                        }}
                        labelId="storetype-select-label"
                        id="storetype-select"
                        aria-label="store-type-select"
                        fullWidth
                        value={value}
                        onChange={(event) => onChange(event.target.value as StoreType)
                        }
                    >
                        {storeTypes.map((store: string) =>
                            <MenuItem value={store} aria-label={store + "-menu-item"}
                                      id={store + "-menu-item"} aria-labelledby={"storetype-select-label"}
                            >
                                store
                            </MenuItem>)}
                    </Select>
                </FormControl>
            </Grid>
    )
}
