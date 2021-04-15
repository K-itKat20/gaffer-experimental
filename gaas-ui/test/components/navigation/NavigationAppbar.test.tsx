import { mount, ReactWrapper } from "enzyme";
import NavigationAppbar from "../../../src/components/navigation-bar/NavigationAppbar";
import React from "react";
import { MemoryRouter } from "react-router-dom";


let component: ReactWrapper;

beforeAll(
    () =>
        (process.env = Object.assign(process.env, {
            REACT_APP_API_PLATFORM: "test",
        }))
);
beforeEach(() => {
    component = mount(
        <MemoryRouter>
            <NavigationAppbar />
        </MemoryRouter>
    );
});
afterEach(() => component.unmount());
afterAll(() => (process.env = Object.assign(process.env, { REACT_APP_API_PLATFORM: "" })));

describe("Navigation Appbar Component", () => {
    it("should display appbar", () => {
        const appbar = component.find("h6");

        expect(appbar).toHaveLength(1);
        expect(appbar.text()).toEqual("Kai: Graph As A Service");
    });

    it("should display a Sign in button in the appbar", () => {
        const signInButton = component.find("button#sign-out-button");

        expect(signInButton.text()).toEqual("Sign out");
    });

    it("should display menu in Navbar", () => {
        const cols = [
            { name: "Add Graph" },
            { name: "View Graphs" },
            { name: "Cluster Namespaces" },
            { name: "User Guide" },
        ];
        const NavLi = component.find("li").at(1);

        NavLi.forEach((li, idx) => {
            const NavIcon = li.find("svg");
            expect(li.text()).toEqual(cols[idx].name);
            expect(NavIcon).toHaveLength(1);
        });
    });

    it("should have navigation link in each list item", () => {
        const Target = [{ href: "/AddGraph" }, { href: "/ViewGraph" }, { href: "/Namespaces" }, { href: "/UserGuide" }];
        const NavUl = component.find("ul").at(1);
        for (var index = 0; index < NavUl.length; index += 1) {
            const anchor = NavUl.find("a").at(index);
            const getAttribute = anchor.getDOMNode().getAttribute("href");
            expect(getAttribute).toBe(Target[index].href);
        }
    });
});
