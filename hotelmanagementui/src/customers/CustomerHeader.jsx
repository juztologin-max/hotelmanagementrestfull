import { NavLink } from "react-router";
import { useNavigate, Link } from "react-router-dom";
import { MenuIcon, LogOut, User, Settings, CreditCard, Menu } from "lucide-react";
import { Button } from "@/components/ui/button";
import {
  Sheet,
  SheetContent,
  SheetTrigger,
  SheetTitle,
  SheetDescription,
  SheetHeader,
  SheetFooter,
  SheetClose,
} from "@/components/ui/sheet";
import { Avatar, AvatarFallback, AvatarImage } from "@/components/ui/avatar";
import {
  DropdownMenu,
  DropdownMenuContent,
  DropdownMenuGroup,
  DropdownMenuItem,
  DropdownMenuLabel,
  DropdownMenuSeparator,
  DropdownMenuTrigger,
} from "@/components/ui/dropdown-menu";
import { Input } from "@/components/ui/input";
import { Label } from "@/components/ui/label";

import useAuth from "@/AuthContext";

export default function Header() {
  const { logout, loginUser } = useAuth();
  const navigate = useNavigate();
  const linkStyles = ({ isActive }) =>
    `text-sm font-medium transition-colors hover:text-foreground/80 ${
      isActive ? "text-foreground font-semibold" : "text-foreground/60"
    }`;

  return (
    <header className="sticky top-0 z-50 w-full border-b bg-background/95 backdrop-blur supports-[backdrop-filter]:bg-background/60">
      <div className="mx-auto max-w-7xl px-4 sm:px-6 lg:px-8 flex h-14 items-center justify-between">
        <div className="flex items-center space-x-6">
          <NavLink to="/" className="font-bold">
            Hotel
          </NavLink>
          <nav className="hidden md:flex items-center space-x-6">
            <NavLink to="/customer/new-booking" className={linkStyles}>
              New Booking
            </NavLink>
          </nav>
        </div>

        <div className="flex items-center space-x-4">
          <div className="hidden md:flex items-center ">
            <DropdownMenu>
              <DropdownMenuTrigger
                render={
                  <Button variant="ghost" size="icon" className="rounded-full">
                    <Avatar>
                      <AvatarImage src="https://github.com/shadcn.png" alt="shadcn" />
                      <AvatarFallback>
                        {loginUser.username.toUpperCase().substring(0, 1)}
                      </AvatarFallback>
                    </Avatar>
                  </Button>
                }
              />
              <DropdownMenuContent className="w-32">
                <DropdownMenuGroup>
                  <DropdownMenuItem>Profile</DropdownMenuItem>
                  <DropdownMenuItem>Billing</DropdownMenuItem>
                  <DropdownMenuItem>Settings</DropdownMenuItem>
                </DropdownMenuGroup>
                <DropdownMenuSeparator />
                <DropdownMenuGroup>
                  <DropdownMenuItem variant="destructive" onClick={logout}>
                    Log out
                  </DropdownMenuItem>
                </DropdownMenuGroup>
              </DropdownMenuContent>
            </DropdownMenu>
          </div>

          <div className="flex md:hidden">
            <Sheet>
              <SheetTrigger
                render={
                  <Button variant="ghost">
                    <Menu />
                  </Button>
                }
              ></SheetTrigger>
              <SheetContent>
                <SheetHeader>
                  <SheetTitle>Navigation</SheetTitle>
                </SheetHeader>

                <Button onClick={() => navigate("/")}>Home</Button>

                <SheetFooter>
                  <Button variant="destructive" onClick={logout}>
                    Log out
                  </Button>
                </SheetFooter>
              </SheetContent>
            </Sheet>
          </div>
        </div>
      </div>
    </header>
  );
}
