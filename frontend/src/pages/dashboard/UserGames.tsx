import Button from '../../components/miscs/Button'
import { Link, useNavigate } from 'react-router'
import { RxExternalLink } from "react-icons/rx";
import { SlOptions } from "react-icons/sl";

type Props = {}

const testGameData = {
  id: 1,
  title: "My Awesome Game",
  description: "An immersive 2D platformer with retro aesthetics and challenging levels.",
  version: "1.0.0",
  path: "https://github.com/yourusername/my-awesome-game.git",
  repositoryName: "my-awesome-game",
  privateRepository: false,
  createdAt: "2025-03-20T12:00:00",
  updatedAt: "2025-03-21T09:30:00"
};


export default function UserGames({ }: Props) {
  const navigate = useNavigate()
  return (
    <>
      <div className='flex flex-col gap-8'>
        <div className="flex items-center justify-between mb-4 px-4">
          <h2 className="text-3xl font-semibold">My Games</h2>
          <Button onClick={() => navigate("new")} className='!text-sm font-medium !bg-gray-50/10 border border-primary' title="New Game"></Button>
        </div>
        <div className='grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 place-items-center gap-8 px-4'>
          <div onClick={() => navigate(`/dashboard/games/${testGameData.id}`)} className='flex flex-col justify-between max-h-40 min-h-40 h-full w-full border border-primary rounded-md cursor-pointer p-6'>
            <div className='flex w-full justify-between'>
              <div className='flex items-center gap-4'>
                <img src="/assets/images/godot_icon.png" className='w-8 h-8 object-cover' alt="Godot icon" />
                <div className='text-sm text-white/80'>
                  <Link className='block hover:underline' to={`${testGameData.id}`}>{testGameData.title}</Link>
                  <Link className='text-xs hover:underline' to={`/games/${testGameData.id}`} target='_blank'>{window.location.origin} <RxExternalLink className='inline-block' />
                  </Link>
                </div>
              </div>
              <SlOptions />

            </div>
            <div>
              <p className='text-sm'>Last build on 24/03/2024</p>
            </div>

          </div>
        </div>
      </div>
    </>
  )
}